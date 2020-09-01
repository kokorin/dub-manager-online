package dmo.client.config {
import mx.logging.targets.LineFormattedTarget;

public class MyTraceTarget extends LineFormattedTarget {
    public function MyTraceTarget() {
        trace("MyTraceTarget")
    }


    override protected function internalLog(message:String):void {
        trace(message);
    }
}
}
