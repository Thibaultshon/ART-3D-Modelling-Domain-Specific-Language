# ART Software Language Engineering Coursework
This is a my coursework for the CS3480 - Software Language Engineering module.

We were tasked with creating two different programming languages for a specific domain. I decided to go for the 3D modelling option, and create a DSL less bloated than OpenSCAD.

Throughout this module we made use of the [ART](https://github.com/AJohnstone2007/ART) (Ambiguity Retained Translation) toolkit

This provided an intuitive way to write logic based programming language specifications in a programmer oriented way.

## About the languages

There are two separate programming language specifications for 2 different 3D modeling related Domain Specific Languages (DSLs):
1. One specification was written using Executable Structural Operational Semantics (SOS)
2. The other using Attribute Grammar rules

Both these languages were Lisp-like languages.
The backend is Java and JavaFX is used to display the 3D modelling. The commands used to interact with the JavaFx plugin were implemented in a stack like way.

## Language 1 Example  (eSOS Semantics)
Features include: 
- Lexical scoping
- Lambdas
- Maps
- Named Functions
- Lists
- While/For Loops
- If/Try/Switch statements
  
Additionally, there are commands such as (Sphere, Cube, Cylinder, Move, Rotate, Colour, Pop) which interact with the JavaFx plugin. 

```
(if (and (> 2 1) true)   (!print 2!) (!print 3!))                     = __done, {=},[2],[{=}] 
!:= x 2! (++ x)                                               = __done, {0=2, 1=3},[],[{x=1}]
!:= x 2! (-- x) !print x!                                  = __done, {0=2, 1=1},[1],[{x=1}]

!:= x 4!  |while (> x 1)  (-- x) !print 2!|                = __done, {0=4,1=3,2=2,3=1},[2,2,2],[{x=3}]

(index  ( list 1 2 3 4 ) 2)                = 3, {=},[],[{=}]

|for !:= i 6! (/= i 4) (-- i) 
      !print i!|"                                                        = __done, {0=6, 1=5, 2=4}, [5, 6], [{=}]



[let (!:= a 3! !:= b 4!) !print a! !print b!]"              = __done, {0=3, 1=4},[4,3],[{=}]

(switch 3 (case 3  !print 4!)
         (case 2 !print 5!
                    (break)))"                                           = __done, {=},[4],[{=}]
!try "(try
      (if (< 1 2)
          (error))
      (catch
          !print 'error'!)
      (end
          !print 'resolve'!))"                                               = __done, {=},["resolve","error"],[{=}]


     : double [(x) !print (* x 2)!]                               
     [func double 2]"                                               = __done,{0=[lambda([x], output(mul(deref(x), 2)))],1=2},[4],[{double=0}]
     

[funcall (\ (x y) !print (+ x y)!) 2 3]                 = __done, {0=2,1=3},[5],[{=}]

|map (\ (x) (return (* (+ x 1) 2))) (1 2 3 4 5)|"      = [4,6,8,10,12], {0=1,1=2,2=3,3=4,4=5},[],[{=}]


(plugin 'init')
(cube 10 20 30)
|for !:= i 0! (/= i 3) (++ i) 
     (sphere 5) (cylinder 20 20)  
      (move  (* i 40) 0 0)
      (colour (% i 2) 0 0)
       (pop) |"
```

## Language 2 Example  (Attribute Grammar)
Features include: 
- Lexical scoping
- Lambdas
- Maps
- Named Functions
- Lists
- Closures(not fully implemented),
- While/For Loops
- If/Try/Switch statements
- Assert statements

Additionally, there are commands such as (Sphere, Cylinder, Cube, Move, Rotate, Colour, Pop) which interact with the JavaFx plugin. 

Functions are also a lot more powerfull in this version. 

```
!try "(+ 2 2 3 3) == 10"
!try "(* 10 2 10) == 200"
!try "(- 100 10 10 10 10 10 10) == 40"
!try "(> 4 3 2) == 1"
!try "(>= 4 4 3 3) == 1"
!try "(assert (/= 2 3))"
!try "(if (= 1 1) 2 (else 0)) == 2"
!try "(switch 3 (case 3 (print (+ 4 2))) (case 3  (print 2)) (case 5 (print 5)) (case 3 (print 222)))"
!try "(try (print 2) (error) (print 5) (catch (print 3) (print 4)))"
!try " (:= a 5) (let((:= a 2) (:= b 3)) (:= a 5) (assert (= a 5)) (assert (= b (- a 2))))"
!try "(:= x  (list 1 2 3 4))  (index x 2) == 3"

 !try "(:= i 2)
       (defun times2 ( b )
           (return (* b 2)))
       (defun times4 ( a )
          (return (* 2 (times2 a))))
        (times4 2) == 8"


 !try "(defun higherOrder ( func1 func2 x)
           (return (+ (func1 x) (func2 x))))
       (defun times2 ( a )
           (return (* a 2)))
       (defun times4 ( a )  
           (return (* a 4)))
       (higherOrder times2 times4 5) ==30"

 !try "(defun recursive ( a )
           (if (= a  0)
           (return 1)
           (else (-- a)
            (recursive a)))
            (print a))
         (recursive 10) == 1"

 !try "(defun functionsList ()
         (return
               (list  
                 (lambda ()(return 2))
                 (lambda () (return 3)))))
       (assert (= (funcall (index (functionsList) 0)) 2))
       (assert (= (funcall (index (functionsList) 1)) 3))"


 !try " 
     (defun makeCounter (x)
        (let ((:= count test))
        (print 'test')
          (let ((:= z 1))
          (return (list
               (lambda (y) (:= count (+ count y z))(++ z))
               (lambda () (return count)))))))
        (defun increaseCounter (counter x)
               (funcall (index counter 0) x))

         (defun getCount (counter)
               (return (funcall (index counter 1))))
         (:= test 3)
        (:= counter (makeCounter 5))
        (increaseCounter counter 1)
        (increaseCounter counter 2)
        (getCount counter) == 9"


 !try " 
     (defun makeCounter (x)
        (let ((:= count x))
          (return (list
               (lambda (y) (:= count (+ count y)))
               (lambda () (return count))))))
        (defun increaseCounter (counter x)
               (funcall (index counter 0) x))

         (defun getCount (counter)
               (return (funcall (index counter 1))))
        (:= counter (makeCounter 5))
        (increaseCounter counter 1)
        (increaseCounter counter 2)
        (getCount counter) == 8"


    (plugin 'init')
      (defun makeCounter (x)
       (let ((:= count x))
         (return (list
              (lambda (y) (:= count (+ count y)))
              (lambda () (return count))))))
              
       (defun increaseCounter (counter x)
              (funcall (index counter 0) x))
       (defun getCount (counter)
              (return (funcall (index counter 1))))
              
       (defun wheelPair (dist1 dist2)
          (cylinder 30 10)
          (colour 1 1 1) 
          (cylinder 10 30)
          (colour 0 1 0) 
          (rotate 'z' 90)
          (move   dist1 dist2 0)
          (pop))

        (defun body ()
         (cylinder  50 500)
         (rotate 'x' 0)
         (move 50  200   (- 40))
         (colour 1 1 0)
         (pop))

       (defun front ()
         (cylinder 20 50)
         (rotate 'x' 90)
         (colour 1 1 0)
         (move 0 0 (- 40))
         (sphere 45)
         (colour 1 0 0)
         (move 50  (- 20)   (- 40))
         (pop))
         
       (defun middle ()
         (cube 80 250 100)
         (move 50 250   (- 60))
         (colour 0 1 0)
         (pop))

      (defun wheels()
       (for (:= counter  (makeCounter 0)) (< (getCount counter)  4) (increaseCounter counter 1)
             (let ((:= vertDist (* 150 (getCount counter))))
             (wheelPair 0 vertDist)
             (wheelPair 100 vertDist))) (pop))


       (body) (front) (middle)  (wheels) 



```

