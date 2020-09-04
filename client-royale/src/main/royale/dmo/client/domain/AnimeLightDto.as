package dmo.client.domain {

public class AnimeLightDto {
    public var id:Number = 0;
    public var type:String = null;

    [ArrayElementType("dmo.client.domain.AnimeTitleDto")]
    public var titles:Array = new Array();
}

}
