package dmo.client.domain {
import mx.collections.ArrayCollection;

public class PageDtoOfAnimeLightDto {
    public var number:Number = 0;
    public var numberOfElements:Number = 0;
    public var size:Number = 0;
    public var totalElements:Number = 0;
    public var totalPages:Number = 0;

    [ArrayElementType("dmo.client.domain.AnimeLightDto")]
    public var content:ArrayCollection;
}

}
