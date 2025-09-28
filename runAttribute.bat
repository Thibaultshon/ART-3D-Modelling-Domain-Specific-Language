javac --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls -cp .;art.jar ARTValuePlugin.java
java   --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls  -cp .;art.jar uk.ac.rhul.cs.csle.art.ART Attribute.art !generate actions
javac --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls -cp .;art.jar ARTGeneratedActions.java
java --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls  -cp .;art.jar uk.ac.rhul.cs.csle.art.ART !interpreter attributeAction Attribute.art
rem javac --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls -cp .;art.jar ARTVAluePlugin.java
rem java  --module-path="C:\home\openJFX\javafx-sdk-17.0.14\lib" --add-modules=javafx.controls -cp .;art.jar uk.ac.rhul.cs.csle.art.ARTFX reduction.art
