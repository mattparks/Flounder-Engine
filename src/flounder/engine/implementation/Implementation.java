package flounder.engine.implementation;

import flounder.engine.*;
import flounder.maths.*;

/**
 * The implementation of the engine and game loop.
 */
public class Implementation implements IModule {
	private IGame game;
	private ICamera camera;
	private IRendererMaster renderer;

	private boolean closedRequested;
	private Delta delta;
	private Timer timerLog;

	/**
	 * Creates a new implementation of the engine and game loop.
	 *
	 * @param game The game to be run with the engine.
	 * @param camera The main camera to use.
	 * @param renderer The master renderer to render with.
	 */
	public Implementation(IGame game, ICamera camera, IRendererMaster renderer) {
		this.game = game;
		this.camera = camera;
		this.renderer = renderer;

		closedRequested = false;
		delta = new Delta();
		timerLog = new Timer(1.0f);
	}

	@Override
	public void init() {
		renderer.init();
		camera.init();
		game.init();
	}

	@Override
	public void update() {
		delta.update();

		game.update();
		camera.update(game.getFocusPosition(), game.getFocusRotation(), game.isGamePaused());

		if (timerLog.isPassedTime()) {
			FlounderEngine.getLogger().log(Maths.roundToPlace(1.0f / getDelta(), 2) + "fps");
			timerLog.resetStartTime();
		}

		renderer.render();
	}

	@Override
	public void profile() {

	}

	public IGame getGame() {
		return game;
	}

	public ICamera getCamera() {
		return camera;
	}

	public IRendererMaster getRendererMaster() {
		return renderer;
	}

	public float getDelta() {
		return delta.getDelta();
	}

	public float getDeltaTime() {
		return delta.getTime();
	}

	public boolean isRunning() {
		return !closedRequested && !FlounderEngine.getDevices().getDisplay().isClosed();
	}

	@Override
	public void dispose() {
		game.dispose();
		renderer.dispose();
	}
}