package speech.types;

import com4j.Com4jObject;
import com4j.GUID;
import com4j.IID;
import com4j.VTID;

@IID("{6D5140C1-7436-11CE-8034-00AA006009FA}")
public interface IServiceProvider extends Com4jObject
{
    // Methods:
    /**
     * @param guidService Mandatory GUID parameter.
     * @param riid Mandatory GUID parameter.
     * @return Returns a value of type com4j.Com4jObject
     */

    @VTID(3)
    com4j.Com4jObject remoteQueryService(GUID guidService, GUID riid);

    // Properties:
}
