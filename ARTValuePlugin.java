


import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.PointLight;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import uk.ac.rhul.cs.csle.art.fx.Transformer3D;
import uk.ac.rhul.cs.csle.art.term.AbstractValuePlugin;
import uk.ac.rhul.cs.csle.art.util.Util;

import java.util.List;
import java.util.ArrayList;
import javafx.application.Platform;


public class ARTValuePlugin extends AbstractValuePlugin {

  public Group root = new Group();
  public Scene scene;
  public Stage primaryStage;
  final PerspectiveCamera camera = new PerspectiveCamera(true);
  final  int  initCamDist  = 500;
  final   Transformer3D world =  new Transformer3D();
  final   Transformer3D camRot =  new Transformer3D();
  final   Transformer3D camPan =  new Transformer3D();

  ArrayList<Shape3D> shapeStack = new ArrayList<Shape3D>();

  @Override
  public String description() {
    return "Own plugin";
  }

  @Override
  public Object plugin(Object... args) {
    switch ((String) args[0]) {
    case "init":
      System.out.println("Plugin initialised");
      init();
      drawAxis();
      return __done;

    case "type":
      for (var i : args)
        System.out.println(i);
      return __done; 

    case "invert":
      return -(Integer) args[1];

    case "cube":
      shapeStack.add( makeBox((int)args[1],(int)args[2],(int)args[3]));
      return  __done;

    case "cylinder":
      shapeStack.add( makeCylinder((int)args[1],(int)args[2]));
      return  __done;

    case "sphere":
      shapeStack.add(makeSphere((int)args[1]));
      return  __done;
  
    case "rotate":
      switch((String)args[1]){
      case "x":
        rotate(Rotate.X_AXIS,(int)args[2]);
        break;
      case "y":                           
        rotate(Rotate.Y_AXIS,(int)args[2]);
        break;
      case "z":                           
        rotate(Rotate.Z_AXIS,(int)args[2]);
        break;


      }
      return  __done;
  
    case "move":
      move((int)args[1],(int)args[2],(int)args[3]);
      return  __done;
      
    case "scale":
      scale((int)args[1],(int)args[2],(int)args[3]);
      return  __done;

    case "colour":
      colour((int)args[1],(int)args[2],(int)args[3]);
      return  __done;

    case "pop":
      pop();
      return __done;


    default:
      Util.fatal("Unknown plugin operation: " + args[0]);
      return __bottom;
    }
  }

  public Box  makeBox(int sizeX, int sizeY, int sizeZ){
    Box box = new Box(sizeX,sizeY,sizeZ);
    root.getChildren().add(box);
    return box;

  }

  public Cylinder makeCylinder(int radius, int length){
  Cylinder cylinder = new Cylinder(radius, length);
   root.getChildren().add(cylinder);
   return cylinder;

  }     

  public Sphere makeSphere(int radius){
     Sphere sphere = new Sphere(radius);
     root.getChildren().add(sphere);
    return sphere;

  }


  public void  move(int vecx, int vecy, int vecz){
    for(Shape3D shape:shapeStack){
        shape.setTranslateX(shape.getTranslateX() + vecx);
        shape.setTranslateY(shape.getTranslateY() + vecy);
        shape.setTranslateZ(shape.getTranslateZ() + vecz);
    }
  }

  public void  colour(int vecx, int vecy, int vecz){
    Color  colour = new Color(vecx,vecy,vecz,1.0);
    PhongMaterial material = new PhongMaterial();
    for(Shape3D shape:shapeStack){
        material.setDiffuseColor(colour);
        shape.setMaterial(material);


    }
  }


  
  public void  rotate(Point3D axis,int angle){
    for(Shape3D shape:shapeStack){
      shape.setRotate(shape.getRotate() + angle);
      shape.setRotationAxis(axis);

    }
  }

  public void  scale(int vecx, int vecy, int vecz){
    for(Shape3D shape:shapeStack){
      shape.setScaleX(shape.getScaleX() + vecx);
      shape.setScaleY(shape.getScaleY() + vecy);
      shape.setScaleZ(shape.getScaleZ() + vecz);
    }
  }


  public void pop(){
    shapeStack = new ArrayList<>();
  }



  private void init(){
    try {
      Platform.startup(() -> {}); //to work for attribute.art  ?
    }catch (IllegalStateException e) {
    }
    Platform.runLater(() -> {
        root.getChildren().add(world);
        initCamera();
        primaryStage = new Stage();
        scene =  new Scene(root,500,300,true);
        scene.setCamera(camera);
        scene.setFill(Color.DARKBLUE);
        handleMouse(scene,world);
        primaryStage.setScene(scene);
        primaryStage.setTitle("3d modeling in art");
        primaryStage.show();});

  }


  

  private void initCamera(){
    root.getChildren().add(camRot);
    camRot.getChildren().add(camera);
    camRot.getChildren().add(camPan);
    camPan.getChildren().add(camera);
    camRot.setRx(60);
    camRot.setRz(60);
    camera.setTranslateZ(-initCamDist);
    camera.setNearClip(0.1);
    camera.setFarClip(2000.0);
  }

  private void drawAxis(){
    final int width = 2;
    final int length =500;
    final Box xAxis = new Box(length,width,width);
    final Box yAxis = new Box(width,length,width);
    final Box zAxis = new Box(width,width,length);
    
    final Box boxes [] ={xAxis,yAxis,zAxis};
    final Color colours[] = {Color.RED,Color.BLUE,Color.GREEN};

    for (int i =0;i <3;i++){
      Box box = boxes[i];
      PhongMaterial material = new PhongMaterial();
      material.setDiffuseColor(colours[i]);
      material.setSpecularColor(colours[i]);
      box.setMaterial(material);
      box.setCullFace(CullFace.NONE);
      world.getChildren().add(box);
    }
  }

 

  double mousePosX;
  double mousePosY;
  double mouseOldX;
  double mouseOldY;
  double mouseDeltaX;
  double mouseDeltaY;


  private void handleMouse(Scene scene, final Node root) {
 
    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
          mousePosX = me.getSceneX();
          mousePosY = me.getSceneY();
          mouseOldX = me.getSceneX();
          mouseOldY = me.getSceneY();
        }
      });
    scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
          mouseOldX = mousePosX;
          mouseOldY = mousePosY;
          mousePosX = me.getSceneX();
          mousePosY = me.getSceneY();
          mouseDeltaX = (mousePosX - mouseOldX); 
          mouseDeltaY = (mousePosY - mouseOldY);
          double modifier = 0.8;
          if (me.isPrimaryButtonDown()) {
            if (me.isAltDown()){
              double z = camera.getTranslateZ();
              double newZ = z + mouseDeltaY * modifier;
              camera.setTranslateZ(newZ);
            }else{
              camRot.rz.setAngle(camRot.rz.getAngle() -  mouseDeltaX*modifier);  
              camRot.rx.setAngle(camRot.rx.getAngle() + mouseDeltaY*modifier);
            }
          }
          else if (me.isSecondaryButtonDown()) {

            camPan.t.setX(camPan.t.getX() + mouseDeltaX * modifier); 
            camPan.t.setY(camPan.t.getY() + mouseDeltaY * modifier);

          }
        }
      }); 

  }

}
