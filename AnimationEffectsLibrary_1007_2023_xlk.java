// 代码生成时间: 2025-10-07 20:23:47
 * The Spark framework is typically used for building rich internet applications
 * and this library provides a set of methods to add animation effects.
 *
 * @author Your Name
 * @version 1.0
 */
package com.spark.animation;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Duration;

public class AnimationEffectsLibrary {

    /**<ol>
     * Applies a fade transition effect on a given node.
     *
     * @param node The node to apply the fade effect on.
     * @param duration The duration of the fade effect.
     */
    public void applyFadeEffect(Node node, Duration duration) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null for fade effect");
        }
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        fadeTransition.play();
    }

    /**<ol>
     * Applies a scale transition effect on a given node.
     *
     * @param node The node to apply the scale effect on.
     * @param scale The scale factor to apply.
     * @param duration The duration of the scale effect.
     */
    public void applyScaleEffect(Node node, double scale, Duration duration) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null for scale effect");
        }
        ScaleTransition scaleTransition = new ScaleTransition(duration, node);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.setToZ(scale);
        scaleTransition.play();
    }

    /**<ol>
     * Applies a translate transition effect on a given node.
     *
     * @param node The node to apply the translate effect on.
     * @param x The horizontal translate distance.
     * @param y The vertical translate distance.
     * @param duration The duration of the translate effect.
     */
    public void applyTranslateEffect(Node node, double x, double y, Duration duration) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null for translate effect");
        }
        TranslateTransition translateTransition = new TranslateTransition(duration, node);
        translateTransition.setByX(x);
        translateTransition.setByY(y);
        translateTransition.play();
    }

    /**<ol>
     * Applies a Gaussian blur effect on a given node.
     *
     * @param node The node to apply the blur effect on.
     */
    public void applyGaussianBlurEffect(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null for Gaussian blur effect");
        }
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(10);
        node.setEffect(blur);
    }

    // Additional animation effects can be added here...

}
