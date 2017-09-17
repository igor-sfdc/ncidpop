#!/bin/sh
# Third Party Text-To-Speech Name Script
# Description: Calls a third party text-to-speech application for purposes 
# of reading data fed into the script.
# Author: Chris Lenderman

echo $1 | /usr/bin/festival --tts
