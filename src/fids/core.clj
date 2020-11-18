(ns fids.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :angle 0})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)})

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  ; Set circle color.
  (q/fill (:color state) 255 255)
  ; Calculate x and y coordinates of the circle.
  (let [angle (:angle state)
        x (* 150 (q/cos angle))
        y (* 150 (q/sin angle))]
    ; Move origin point to the center of the sketch.
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      ; Draw the circle.
      (q/ellipse x y 100 100))))

(defn matrix [a b e c d f]
  [[a b e]
   [c d f]])

(defn iter [matrix- x y]
  (let [[a b e] (first matrix-)
        [c d f] (second matrix-)]
    [(+ (* a x) (* b y) e)
     (+ (* c x) (* d y) f)]))

(def m0 (matrix 0.5 0.5 0.0
                0.5 0.5 0.0))
(def m1 (matrix 0.5 0.5 0.5
                0.5 0.5 0.0))
(def m2 (matrix 0.5 0.5 0.25
                0.5 0.5 0.5))

(def transforms [m0 m1 m2])

(def basis [[1.0 0.0]
            [0.0 0.0]
            [0.0 1.0]])
 (for [m transforms]
    (for [b basis]
      {b
       (iter m (first b) (second b))}))








(q/defsketch fids
  :title "You spin my circle right round"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
