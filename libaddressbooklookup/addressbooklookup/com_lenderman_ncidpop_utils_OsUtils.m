//
//  com_lenderman_ncidpop_utils_OsUtils.m
//  addressbooklookup
//
//  Copyright (c) 2015 Chris Lenderman. All rights reserved.
//  Copyright (c) 2008 Nicholas Riley. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AddressBook/AddressBook.h>
#include "com_lenderman_ncidpop_utils_OsUtils.h"
#include <time.h>

@interface ABPhoneFormatter : NSObject
- (id)stringForObjectValue:(id)arg1;
@end

@interface ABPhoneFormatter (PHXSingletonAdditions)
+ (id)sharedPhoneFormatter;
@end

/*
 * Struct for aggregating a person and label
 */
struct PersonAndLabel
{
    ABPerson* person;
    NSString* label;
};

/*
 * Converts the passed in number to be a number that will be matchable in the address book
 * NOTE THAT THE RETURNED STRING IS NOT AUTORELEASED!
 */
static NSString *asMatchableNumber(NSString *number)
{
    static NSCharacterSet *notDecimalDigitCharacterSet = nil;
    static NSCharacterSet *zeroCharacterSet = nil;
    
    NSScanner *scanner = [[NSScanner alloc] initWithString:number];
    NSMutableString *matchableNumber = [[NSMutableString alloc] initWithCapacity:[number length]];
    NSString *digits;
    
    if (notDecimalDigitCharacterSet == nil)
    {
        notDecimalDigitCharacterSet = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
        zeroCharacterSet = [NSCharacterSet characterSetWithCharactersInString:@"0"];
    }
    [scanner setCharactersToBeSkipped:notDecimalDigitCharacterSet];
    
    // skip leading 0s (considered part of area/city code in some countries, but really a prefix)
    [scanner scanCharactersFromSet:zeroCharacterSet intoString:NULL];
    
    while (![scanner isAtEnd])
    {
        if (![scanner scanUpToCharactersFromSet:notDecimalDigitCharacterSet intoString:&digits])
        {
            continue;
        }
        [matchableNumber appendString:digits];
    }
    [scanner release];
    
    if ([matchableNumber length] < 3)
    {
        [matchableNumber release];
        return nil;
    }
    
    return matchableNumber;
}

/*
 * Convert a Java String into a NSString.
 */
static NSString *createNSStringFromJavaString(JNIEnv *env, jstring javaString)
{
    if (javaString == NULL)
    {
        return nil;
    }
    
    NSString *cocoaString;
    const char *nativeString;

    nativeString = (*env)->GetStringUTFChars(env, javaString, JNI_FALSE);
    cocoaString = [NSString stringWithUTF8String:nativeString];
    
    return cocoaString;
}

/*
 * Convert a NSString into a Java String
 */
static jstring createJavaStringFromNSString(JNIEnv *env, NSString *nativeStr)
{
    if (nativeStr == nil)
    {
        return NULL;
    }
    // Note that length returns the number of UTF-16 characters,
    // which is not necessarily the number of printed/composed characters
    jsize buflength = (int)[nativeStr length];
    unichar buffer[buflength];
    [nativeStr getCharacters:buffer];
    jstring javaStr = (*env)->NewString(env, (jchar *)buffer, buflength);

    return javaStr;
}

/*
 * Given an address book person, return a name
 */
static NSString *nameForPerson(ABPerson *person)
{
    unsigned flags = [[person valueForProperty:kABPersonFlags] unsignedIntValue];
    enum { lastCommaFirst, lastFirst, firstLast } ordering = lastCommaFirst;
    
    if ((flags & kABShowAsMask) == kABShowAsCompany)
    {
        NSString *companyName = [person valueForProperty:kABOrganizationProperty];
        if ([companyName length] > 0)
        {
            return companyName;
        }
    }
    
    if ((flags & kABNameOrderingMask) == kABLastNameFirst)
    {
        ordering = lastFirst;
    }
    else if ((flags & kABNameOrderingMask) == kABLastNameFirst)
    {
        ordering = firstLast;
    }
    
    NSString *first = [person valueForProperty:kABFirstNameProperty];
    NSString *middle = [person valueForProperty:kABMiddleNameProperty];
    NSString *last = [person valueForProperty:kABLastNameProperty];
    
    if (middle != nil)
    {
        first = [NSString stringWithFormat:@"%@ %@", first, middle];
    }
    
    if (first == nil)
    {
        return last;
    }
    
    if (last == nil)
    {
        return first;
    }
    
    switch (ordering)
    {
        case lastCommaFirst:
            return [NSString stringWithFormat:@"%@, %@", last, first];
        case lastFirst:
            return [NSString stringWithFormat:@"%@ %@", last, first];
        case firstLast:
            return [NSString stringWithFormat:@"%@ %@", first, last];
    }
    
    return nil;
}

/*
 * Given an image data object, return the extension type
 */
static NSString* contentTypeForImageData(NSData *data)
{
    uint8_t c;
    [data getBytes:&c length:1];
    
    switch (c) {
        case 0xFF:
            return @"jpeg";
        case 0x89:
            return @"png";
        case 0x47:
            return @"gif";
        case 0x49:
        case 0x4D:
            return @"tiff";
    }
    return nil;
}
/*
 * Given a number, returns a person object with corresponding label
 * NOTE THAT THE PersonAndLabel OBJECT RETURNED SHOULD BE DEALLOCATED BY CALLER!
 */
struct PersonAndLabel *personForNumber(NSString *number)
{
    struct PersonAndLabel *pl = (struct PersonAndLabel*)malloc(sizeof(struct PersonAndLabel));
    pl->person = nil;
    pl->label = nil;
    
    NSString *matchableNumber = asMatchableNumber(number);
    if (matchableNumber != nil)
    {
        static ABAddressBook *addressBook = nil;
        if (addressBook == nil)
        {
            addressBook = [ABAddressBook sharedAddressBook];
        }
        NSArray *people = [addressBook people];
        NSEnumerator *personEnumerator = [people objectEnumerator];
        ABPerson *person;
        
        while ((person = [personEnumerator nextObject]) != nil)
        {
            ABMultiValue *numbers = [person valueForProperty:kABPhoneProperty];
            
            for (int i = 0 ; i < [numbers count] ; i++)
            {
                NSString *numberToMatch = asMatchableNumber([numbers valueAtIndex:i]);
                
                if (numberToMatch == nil)
                {
                    continue;
                }
                
                if ( ([numberToMatch length] >= 6 || [matchableNumber length] < 6) &&
                    ([numberToMatch rangeOfString: matchableNumber].length > 0 ||
                     [matchableNumber rangeOfString: numberToMatch].length > 0))
                {
                    [numberToMatch release];
                    NSString *label = [numbers labelAtIndex:i];
                    if (label != nil)
                    {
                        pl->person = person;
                        pl->label = label;
                        return pl;
                    }
                }
                [numberToMatch release];
            }
        }
    }
    return pl;
}

/*
 * Class:     com_lenderman_ncidpop_utils_OsUtils
 * Method:    getMacintoshAddressBookAlias
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_lenderman_ncidpop_utils_OsUtils_getMacintoshAddressBookAlias
(JNIEnv * env, jclass thisClass, jstring cStringNumber)
{
    NSString *number = createNSStringFromJavaString(env, cStringNumber);
    struct PersonAndLabel* pl = personForNumber(number);
    
    if (pl->person != nil && pl->label != nil)
    {
        NSString *name = nameForPerson(pl->person);
        NSString *prettyName = [[NSString alloc] initWithFormat:@"%@ (%@)", name,
                                ABLocalizedPropertyOrLabel(pl->label)];
        jstring returnString = createJavaStringFromNSString(env, prettyName);

        [prettyName release];
        free(pl);
        return returnString;
    }
    free(pl);
    return nil;
}

/*
 * Class:     com_lenderman_ncidpop_utils_OsUtils
 * Method:    getMacintoshAddressBookImage
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_lenderman_ncidpop_utils_OsUtils_getMacintoshAddressBookImage
(JNIEnv * env, jclass thisClass, jstring cStringNumber)
{
    NSString *number = createNSStringFromJavaString(env, cStringNumber);
    struct PersonAndLabel* pl = personForNumber(number);
    
    if (pl->person != nil)
    {
        NSData  *imgData = [pl->person imageData];

        NSString *guid = [[NSProcessInfo processInfo] globallyUniqueString] ;
        NSString *filename = [NSTemporaryDirectory() stringByAppendingPathComponent:[NSString stringWithFormat:@"%@.%@", guid, contentTypeForImageData(imgData)]];
    
        [imgData writeToFile:filename atomically:YES];
        jstring returnString = createJavaStringFromNSString(env, filename);
    
        free(pl);
        return returnString;
    }
    free(pl);
    return nil;
}

/*
 * Class:     com_lenderman_ncidpop_utils_OsUtils
 * Method:    getMacintoshAddressBookList
 * Signature: ()Ljava/util/HashMap;
 */
JNIEXPORT jobject JNICALL Java_com_lenderman_ncidpop_utils_OsUtils_getMacintoshAddressBookList
(JNIEnv * env, jclass thisClass)
{
    jclass mapClass = (*env)->FindClass(env, "java/util/HashMap");
    
    if(mapClass == NULL)
    {
        return NULL;
    }
    
    jmethodID init = (*env)->GetMethodID(env, mapClass, "<init>", "()V");

    if (init == NULL)
    {
        return NULL;
    }

    jobject hashMap = (*env)->NewObject(env, mapClass, init);

    if (hashMap == NULL)
    {
        return NULL;
    }

    jmethodID put = (*env)->GetMethodID(env, mapClass, "put",
                                        "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    if (put == NULL)
    {
        return NULL;
    }
    
    ABAddressBook *addressBook = [ABAddressBook sharedAddressBook];
    NSArray *people = [addressBook people];
    NSEnumerator *personEnumerator = [people objectEnumerator];
    ABPerson *person;

    while ((person = [personEnumerator nextObject]) != nil)
    {
        ABMultiValue *numbers = [person valueForProperty:kABPhoneProperty];
        
        for (int i = 0 ; i < [numbers count] ; i++)
        {
            NSString *formattedNumber = asMatchableNumber([numbers valueAtIndex:i]);
            
            if (formattedNumber == nil)
            {
                continue;
            }
            
            NSString *label = [numbers labelAtIndex:i];
            NSString *name = nameForPerson(person);

            if (label != nil)
            {
                NSString *prettyName = [[NSString alloc] initWithFormat:@"%@ (%@)", name,
                                        ABLocalizedPropertyOrLabel(label)];
                jstring key = createJavaStringFromNSString(env, formattedNumber);
                jstring value = createJavaStringFromNSString(env, prettyName);
                
                (*env)->CallObjectMethod(env, hashMap, put, key, value);
                (*env)->DeleteLocalRef(env, value);
                (*env)->DeleteLocalRef(env, key);

                [prettyName release];
            }
            [formattedNumber release];
        }
    }
    return hashMap;
}

/*
 * Class:     com_lenderman_ncidpop_utils_OsUtils
 * Method:    launchMacintoshAddressBook
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_lenderman_ncidpop_utils_OsUtils_launchMacintoshAddressBook
(JNIEnv * env, jclass thisClass, jstring cStringNumber)
{
    NSString *number = createNSStringFromJavaString(env, cStringNumber);
    
    NSString *matchableNumber = asMatchableNumber(number);
    if (matchableNumber != nil)
    {
        static ABAddressBook *addressBook = nil;
        if (addressBook == nil)
        {
            addressBook = [ABAddressBook sharedAddressBook];
        }
        NSArray *people = [addressBook people];
        NSEnumerator *personEnumerator = [people objectEnumerator];
        
        ABPerson *person;
        
        while ((person = [personEnumerator nextObject]) != nil)
        {
            ABMultiValue *numbers = [person valueForProperty:kABPhoneProperty];
            int i;
            
            for (i = 0 ; i < [numbers count] ; i++)
            {
                NSString *numberToMatch = asMatchableNumber([numbers valueAtIndex:i]);
                
                if (numberToMatch == nil)
                {
                    continue;
                }
                
                if (([numberToMatch length] >= 6 || [matchableNumber length] < 6) &&
                    ([numberToMatch rangeOfString: matchableNumber].length > 0 ||
                     [matchableNumber rangeOfString: numberToMatch].length > 0))
                {
                    [numberToMatch release];
                    NSURL *theUrl = [NSURL URLWithString:[NSString stringWithFormat:@"addressbook://%@", [person uniqueId]]];
                    [[NSWorkspace sharedWorkspace] openURL:theUrl];
                    return;
                }
                [numberToMatch release];
            }
        }
    }
}