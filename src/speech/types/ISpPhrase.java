package speech.types;

import com4j.Com4jObject;
import com4j.IID;
import com4j.VTID;

/**
 * ISpPhrase Interface
 */
@IID("{1A5C0354-B621-4B5A-8791-D306ED379E53}")
public interface ISpPhrase extends Com4jObject
{
    // Methods:
    /**
     * @param dwValueTypes Mandatory int parameter.
     */

    @VTID(6)
    void discard(int dwValueTypes);

    // Properties:
}
