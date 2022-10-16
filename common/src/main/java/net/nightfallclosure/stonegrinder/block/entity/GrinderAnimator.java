package net.nightfallclosure.stonegrinder.block.entity;

import java.util.LinkedList;

import static net.nightfallclosure.stonegrinder.constants.GrinderAnimationConstants.*;

public class GrinderAnimator {
    private LinkedList<Integer> animationFrames;
    private int currentFrame;

    public GrinderAnimator() {
        animationFrames = new LinkedList<>();
        currentFrame = defaultFrame;
    }

    public int getCurrentFrame() {
        return this.currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public LinkedList<Integer> getAnimationFrames() {
        return this.animationFrames;
    }

    public void setAnimationFrames(LinkedList<Integer> animationFrames) {
        this.animationFrames = animationFrames;
    }

    public int getLastFrame() {
        return this.animationFrames.isEmpty() ? this.currentFrame : this.animationFrames.getLast();
    }

    public void queueNewBlockAnimation() {
        this.queueTransitionToHighestPositionFrame();
        this.queueMultipleOfOneFrame(highestPositionFrame, 7);
        this.queueTransitionToGrindingFrame();
    }

    public void queueTransitionToDefaultFrame() {
        if (this.currentFrame < defaultFrame) {
            for (int i = this.currentFrame + 1; i <= defaultFrame; ++i) {
                this.animationFrames.add(i);
            }
        }
        else {
            for (int i = this.currentFrame - 1; i >= defaultFrame; --i) {
                this.animationFrames.add(i);
            }
        }
    }

    public void queueTransitionToHighestPositionFrame() {
        for (int i = this.getLastFrame() - 1; i >= highestPositionFrame; --i) {
            this.animationFrames.add(i);
        }
    }

    public void queueTransitionToGrindingFrame() {
        for (int i = this.getLastFrame() + 1; i <= grindingFrame; ++i) {
            this.animationFrames.add(i);
        }
    }

    public void queueMultipleOfOneFrame(int frame, int amount) {
        for (int i = 0; i < amount; ++i) {
            this.animationFrames.add(frame);
        }
    }
}
