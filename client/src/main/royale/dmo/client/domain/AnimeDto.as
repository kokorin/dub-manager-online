package dmo.client.domain {
import mx.collections.ArrayCollection;

public class AnimeDto {
    public var id:Number = 0;
    public var type:String = null;

    [ArrayElementType("dmo.client.domain.AnimeTitleDto")]
    public var titles:ArrayCollection;
}
}
