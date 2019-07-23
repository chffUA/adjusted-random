# Adjusted Random

Randomness generator affected by past outcomes. Depending on user settings, it can shorten or lengthen potential win/loss streaks while maintaining a win/loss probability similar to the intended one.

### Basic concepts

- An instance of the ```AdjustedRandom``` class keeps track of how many times it has rolled a success or a failure in a row (**streak**). 

- ```AdjustedRandom``` will adjust the probability of success for the next roll based on the current streak (and if it's a positive or negative one).

- The actual adjustment made is defined in the ```adjust(double chance)``` method of a class that implements the ```Adjuster``` interface. 

- An ```AdjustedRandom``` instance accepts an Adjuster, which it uses to modify probability based on the current streak. Users can select existing adjuster classes (**parabola** and **branched**) or create custom adjuster classes.

- The ```diagnostic(double chance, int tries)``` method may help test the impact of a given adjuster for a given starting probability.

See the documentation included in the source code for a much more in-depth explanation of each method and class.

### Examples

Diagnostic method using the ```BranchedAdjuster```:

**chance** = 0.15, **tries** = 100,000

![](https://i.imgur.com/qrFYq7l.png)

**chance** = 0.02, **tries** = 100,000

![](https://i.imgur.com/eRBzNF1.png)

**chance** = 0.60, **tries** = 100,000

![](https://i.imgur.com/Gz9Gm8i.png)