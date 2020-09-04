package dmo.client.domain {

public class EpisodeDto {
    public var id:Number = 0;
    public var type:String = null;
    public var number:Number = 0;
    public var airDate:Date = null;
    public var length:Number = 0;

    [ArrayElementType("dmo.client.domain.EpisodeTitleDto")]
    public var titles:Array;
}

}
