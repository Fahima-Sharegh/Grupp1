package runner;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


//The advantages with this Cucumber approach is that much of the code that was repeated in the unit test can easily be reused. 
//The code sort of refactored itself, which was a very nice surprise.

//Getting the project to run was probably the hardest thing all along. It took me way to long to find out about the glue.

@RunWith(Cucumber.class)@CucumberOptions(
    features = "src/test/resources/features",
    glue = "steps")
public class RunCucumberIT {
}
