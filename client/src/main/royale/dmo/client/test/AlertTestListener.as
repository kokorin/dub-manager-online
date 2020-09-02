package dmo.client.test {
import org.apache.royale.jewel.Alert;
import org.apache.royale.test.runners.notification.Failure;
import org.apache.royale.test.runners.notification.IRunListener;
import org.apache.royale.test.runners.notification.Result;

public class AlertTestListener implements IRunListener {
    private var failureCount:int = 0;

    public function AlertTestListener() {
    }

    public function testStarted(description:String):void {
    }

    public function testFinished(description:String):void {
        if (failureCount > 0) {
            Alert.show("Test Failed: " + failureCount);
        }
    }

    public function testFailure(failure:Failure):void {
        failureCount++;
    }

    public function testIgnored(description:String):void {
    }

    public function testRunStarted(description:String):void {
    }

    public function testRunFinished(result:Result):void {
    }
}
}
