package de.pdobrowo.game.animation;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class GameLoopTimer extends AnimationTimer {

	public long pauseStart;
	public long animationStart;
	public long lastFrameTimeNanos;
	DoubleProperty animationDuration = new SimpleDoubleProperty(0);
	public boolean isPaused;
	public boolean isActive;
	public boolean pauseScheduled;
	public boolean playScheduled;
	public boolean restartScheduled;
	
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public DoubleProperty animationDurationProperty() {
		return animationDuration;
	}
	
	public void pause() {
		if(!isPaused) {
			pauseScheduled = true;
		}
	}
	
	public void play() {
		if(isPaused) {
			playScheduled = true;
		}
	}
	
	@Override
	public void start() {
		super.start();
		isActive = true;
		restartScheduled = true;
	}
	
	@Override
	public void stop() {
		super.stop();
		pauseStart = 0;
		isPaused = false;
		isActive = false;
		pauseScheduled = false;
		playScheduled = false;
		animationDuration.set(0);
	}
	
	@Override
	public void handle(long now) {
		if(pauseScheduled) {
			pauseStart = now;
			isPaused = true;
			pauseScheduled = false;
		}
		if(playScheduled) {
			animationStart = animationStart + (now-pauseStart);
			isPaused = false;
			playScheduled = false;
		}
		if (restartScheduled) {
			isPaused = false;
			animationStart = now;
			restartScheduled = false;
		}
		if(!isPaused) {
			long aniDuration = now - animationStart;
			animationDuration.set(aniDuration/1e9);
			float secondsSinceLastFrame = (float) ((now - lastFrameTimeNanos) / 1e9);
			if(secondsSinceLastFrame > 0.02) {
				secondsSinceLastFrame = 0.01f;
			}
			lastFrameTimeNanos = now;
			tic(secondsSinceLastFrame);
		}
	} 
	
	public abstract void tic(float secondsSinceLastFrame);

}
