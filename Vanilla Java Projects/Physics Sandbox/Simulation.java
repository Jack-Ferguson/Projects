
/*
 * Author: Jack Ferguson
 * CS 225 Spring 2021
 * 
 * Simulation.java
 * 
 * Manages all particles and the GUI
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Simulation extends Application {

	Timeline animation;

	private double stageWidth, stageHeight;
	private BorderPane display;

	private Pane sandbox;
	private Color sandboxBackgroundColor;

	private Pane sandboxSavePane;
	private ListView<String> saveList;
	private TextField saveNameInput;
	private Button saveButton, loadButton;

	private Pane particleSavePane;
	private ListView<String> particleSaveList;
	private Button saveParticleButton, loadParticleButton;

	private Pane controlPane, inspectorPane;
	private Text environmentControlLabel, gravityControlLabel, gravityXControlLabel, gravityYControlLabel,
			customizerControlLabel, nameControlLabel, radiusControlLabel, massControlLabel, colorControlLabel,
			rControlLabel, gControlLabel, bControlLabel, forcefieldControlLabel, instructionControlLabel;
	private TextField gravityXInput, gravityYInput, nameInput, radiusInput, massInput, rInput, gInput, bInput,
			forcefieldInput;
	private CheckBox specialParticleCheckBox;
	private Button flowButton, resetButton, saveLoadSandboxButton, randomColorButton, saveLoadParticleButton,
			addRandomButton;
	private Circle previewCircle;

	private Text nameInspectorLabel, radiusInspectorLabel, massInspectorLabel, colorInspectorLabel,
			forcefieldInspectorLabel;
	private Button xButton, deleteButton;
	private Kinematic inspectorTargetParticle;
	private Circle inspectorTargetCircle, inspectorPreviewCircle;

	public static Vector gravity = new Vector(0, -250);

	private double timeStep;
	private boolean running;

	private ArrayList<Particle> particles;
	private ArrayList<SpecialParticle> specialParticles;
	private ArrayList<Circle> circles;

	public Simulation() {

		particles = new ArrayList<Particle>();
		specialParticles = new ArrayList<SpecialParticle>();
		circles = new ArrayList<Circle>();
		running = false;
		timeStep = .005;
		animation = new Timeline(new KeyFrame(Duration.millis(timeStep * 1000), e -> simulate()));
		animation.setCycleCount(Timeline.INDEFINITE);

		stageWidth = 500;
		stageHeight = 500;

		display = new BorderPane();
		sandbox = new Pane();

		sandboxBackgroundColor = new Color(map(255 / 2, 0, 255, 0, 1), map(255 / 2, 0, 255, 0, 1),
				map(255 / 2, 0, 255, 0, 1), 1);
		sandbox.setBackground(new Background(new BackgroundFill(sandboxBackgroundColor, null, null)));

		// SANDBOX SAVE PANE SETUP
		sandboxSavePane = new Pane();

		saveList = new ListView<String>();
		saveList.setPrefSize(200, 400);
		saveList.setLayoutX(0);
		saveList.setLayoutY(0);
		saveList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				saveNameInput.setText(newValue);
			}

		});

		saveNameInput = new TextField();
		saveNameInput.setLayoutX(0);
		saveNameInput.setLayoutY(saveList.getPrefHeight());
		saveNameInput.setPrefWidth(115);

		saveButton = new Button("Save");
		saveButton.setLayoutX(115);
		saveButton.setLayoutY(saveList.getPrefHeight());
		saveButton.setOnAction(new SaveHandler());

		loadButton = new Button("Load");
		loadButton.setLayoutX(155);
		loadButton.setLayoutY(saveList.getPrefHeight());
		loadButton.setOnAction(new LoadHandler());

		sandboxSavePane.getChildren().addAll(saveList, saveNameInput, saveButton, loadButton);
		// END SANDBOX SAVE PANE SETUP

		// PARTICLE SAVE PANE SETUP
		particleSavePane = new Pane();

		particleSaveList = new ListView<String>();
		particleSaveList.setPrefSize(200, 400);
		particleSaveList.setLayoutX(0);
		particleSaveList.setLayoutY(0);

		saveParticleButton = new Button("Save");
		saveParticleButton.setLayoutX(0);
		saveParticleButton.setLayoutY(particleSaveList.getPrefHeight());
		saveParticleButton.setOnAction(event -> {
			try {
				FileWriter fw = new FileWriter("particleSaves.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				double radius, mass, forcefield, r, g, b;

				try {
					radius = clamp(Double.parseDouble(radiusInput.getText()), 5, controlPane.getPrefWidth() / 2);
				} catch (Exception e) {
					radius = 5;
				}
				try {
					mass = clamp(Double.parseDouble(massInput.getText()), -1000, 1000);
				} catch (Exception e) {
					mass = 20;
				}
				try {
					forcefield = clamp(Double.parseDouble(forcefieldInput.getText()), -10000, 10000);
				} catch (Exception e) {
					forcefield = 0;
				}
				try {
					r = map(Double.parseDouble(rInput.getText()), 0, 255, 0, 1);
				} catch (Exception e) {
					r = 0;
				}
				try {
					g = map(Double.parseDouble(gInput.getText()), 0, 255, 0, 1);
				} catch (Exception e) {
					g = 0;
				}
				try {
					b = map(Double.parseDouble(bInput.getText()), 0, 255, 0, 1);
				} catch (Exception e) {
					b = 0;
				}
				String toWrite = "" + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + mass + "," + radius
						+ "," + forcefield + "," + r + "," + g + "," + b + "," + nameInput.getText()+","+(specialParticleCheckBox.isSelected() ? "true" : "false");
				bw.append(toWrite);
				bw.newLine();
				bw.close();
				getParticleList();
			} catch (IOException e) {
				System.out.println("Error Saving Particle");
				e.printStackTrace();
			}
		});

		loadParticleButton = new Button("Load");
		loadParticleButton.setLayoutX(40);
		loadParticleButton.setLayoutY(particleSaveList.getPrefHeight());
		loadParticleButton.setOnAction(event -> {
			try {
				FileReader fr = new FileReader("particleSaves.txt");
				BufferedReader br = new BufferedReader(fr);

				for (int i = 0; i < particleSaveList.getSelectionModel().selectedIndexProperty().get(); i++) {
					br.readLine();
				}

				String[] parameters = br.readLine().split(",");

				nameInput.setText(parameters[12]);
				radiusInput.setText(parameters[7]);
				massInput.setText(parameters[6]);
				forcefieldInput.setText(parameters[8]);
				rInput.setText("" + map(Double.parseDouble(parameters[9]), 0, 1, 0, 255));
				gInput.setText("" + map(Double.parseDouble(parameters[10]), 0, 1, 0, 255));
				bInput.setText("" + map(Double.parseDouble(parameters[11]), 0, 1, 0, 255));
				specialParticleCheckBox.setSelected(parameters[13].equals("true"));

				br.close();

			} catch (Exception e) {
				System.out.println("Error Loading Particle");
				e.printStackTrace();
			}
		});

		particleSavePane.getChildren().addAll(particleSaveList, saveParticleButton, loadParticleButton);
		// END PARTICLE SAVE PANE SETUP

		// INSPECTOR PANE SETUP
		inspectorPane = new Pane();

		inspectorPane.setStyle("-fx-background-color: gainsboro");
		inspectorPane.setPrefWidth(200);

		inspectorTargetParticle = new Particle(0, new Point(0, 0), 0);
		((Particle) inspectorTargetParticle).setRadius(0);
		((Particle) inspectorTargetParticle).setColor(0, 1, 0);
		((Particle) inspectorTargetParticle).setName("Temp");

		xButton = new Button("X");
		xButton.setLayoutX(inspectorPane.getPrefWidth() - 30);
		xButton.setLayoutY(7);
		xButton.setOnAction(e -> {
			display.setRight(controlPane);
		});

		nameInspectorLabel = new Text();
		radiusInspectorLabel = new Text();
		massInspectorLabel = new Text();
		colorInspectorLabel = new Text();
		forcefieldInspectorLabel = new Text();

		inspectorPreviewCircle = new Circle();

		deleteButton = new Button("Delete Particle");
		deleteButton.setLayoutX(inspectorPane.getPrefWidth() / 2 - 45);
		deleteButton.setLayoutY(165);
		deleteButton.setOnAction(e -> {
			if (inspectorTargetParticle instanceof SpecialParticle) {
				specialParticles.remove(inspectorTargetParticle);
				for (int i = ((SpecialParticle) inspectorTargetParticle).getCircleIndex(); i < specialParticles
						.size(); i++) {
					specialParticles.get(i).setCircleIndex(specialParticles.get(i).getCircleIndex() - 1);
				}
			} else {
				particles.remove(inspectorTargetParticle);
				for (int i = ((Particle) inspectorTargetParticle).getCircleIndex(); i < particles.size(); i++) {

				}
			}
			circles.remove(inspectorTargetCircle);
			sandbox.getChildren().remove(inspectorTargetCircle);
			display.setRight(controlPane);

			updateBackgroundColor();
		});

		inspectorPane.getChildren().addAll(nameInspectorLabel, radiusInspectorLabel, massInspectorLabel,
				colorInspectorLabel, forcefieldInspectorLabel);
		inspectorPane.getChildren().addAll(xButton, deleteButton);
		inspectorPane.getChildren().addAll(inspectorPreviewCircle);

		// END INSPECTOR PANE SETUP

		// CONTROL PANE SETUP
		controlPane = new Pane();

		controlPane.setStyle("-fx-background-color: gainsboro");
		controlPane.setPrefWidth(200);

		environmentControlLabel = new Text("Simulation Control");
		environmentControlLabel
				.setLayoutX(controlPane.getPrefWidth() / 2 - environmentControlLabel.getLayoutBounds().getWidth() / 2);
		environmentControlLabel.setLayoutY(15);

		flowButton = new Button("Play");
		flowButton.setLayoutX(controlPane.getPrefWidth() / 2 - 85);
		flowButton.setLayoutY(20);
		flowButton.setOnAction(new StartHandler());

		resetButton = new Button("Reset");
		resetButton.setLayoutX(controlPane.getPrefWidth() / 2 - 35);
		resetButton.setLayoutY(20);
		resetButton.setOnAction(new ResetHandler());

		saveLoadSandboxButton = new Button("Save/Load");
		saveLoadSandboxButton.setLayoutX(controlPane.getPrefWidth() / 2 + 20);
		saveLoadSandboxButton.setLayoutY(20);
		saveLoadSandboxButton.setOnAction(new ShowSandboxSaveHandler());

		gravityControlLabel = new Text("Gravity");
		gravityControlLabel
				.setLayoutX(controlPane.getPrefWidth() / 2 - gravityControlLabel.getLayoutBounds().getWidth() / 2);
		gravityControlLabel.setLayoutY(60);

		gravityXControlLabel = new Text("X");
		gravityXControlLabel.setLayoutX(
				controlPane.getPrefWidth() / 2 - gravityXControlLabel.getLayoutBounds().getWidth() / 2 - 35);
		gravityXControlLabel.setLayoutY(85);

		gravityXInput = new TextField("" + gravity.getX());
		gravityXInput.setMaxWidth(70);
		gravityXInput.setLayoutX(gravityXControlLabel.getLayoutX() + 10);
		gravityXInput.setLayoutY(68);
		gravityXInput.setAlignment(Pos.CENTER);
		gravityXInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateGravity();
			}

		});

		gravityYControlLabel = new Text("Y");
		gravityYControlLabel.setLayoutX(
				controlPane.getPrefWidth() / 2 - gravityYControlLabel.getLayoutBounds().getWidth() / 2 - 35);
		gravityYControlLabel.setLayoutY(115);

		gravityYInput = new TextField("" + gravity.getY());
		gravityYInput.setMaxWidth(70);
		gravityYInput.setLayoutX(gravityYControlLabel.getLayoutX() + 10);
		gravityYInput.setLayoutY(98);
		gravityYInput.setAlignment(Pos.CENTER);
		gravityYInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateGravity();
			}

		});

		customizerControlLabel = new Text("Particle Customizer");
		customizerControlLabel
				.setLayoutX(controlPane.getPrefWidth() / 2 - customizerControlLabel.getLayoutBounds().getWidth() / 2);
		customizerControlLabel.setLayoutY(142);

		nameControlLabel = new Text("Name");
		nameControlLabel.setLayoutX(
				controlPane.getPrefWidth() / 2 - customizerControlLabel.getLayoutBounds().getWidth() / 2 - 25);
		nameControlLabel.setLayoutY(170);

		nameInput = new TextField("Particle");
		nameInput.setLayoutX(nameControlLabel.getLayoutX() + nameControlLabel.getLayoutBounds().getWidth() + 10);
		nameInput.setLayoutY(153);
		nameInput.setMaxWidth(100);
		nameInput.setAlignment(Pos.CENTER);

		radiusControlLabel = new Text("Radius");
		radiusControlLabel
				.setLayoutX(controlPane.getPrefWidth() / 2 - radiusControlLabel.getLayoutBounds().getWidth() / 2 - 30);
		radiusControlLabel.setLayoutY(200);

		radiusInput = new TextField("20");
		radiusInput.setLayoutX(radiusControlLabel.getLayoutX() + radiusControlLabel.getLayoutBounds().getWidth() + 10);
		radiusInput.setLayoutY(183);
		radiusInput.setMaxWidth(50);
		radiusInput.setAlignment(Pos.CENTER);
		radiusInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateParticlePreview();
				previewCircle.toBack();
			}

		});

		massControlLabel = new Text("Mass");
		massControlLabel
				.setLayoutX(controlPane.getPrefWidth() / 2 - massControlLabel.getLayoutBounds().getWidth() / 2 - 30);
		massControlLabel.setLayoutY(230);

		massInput = new TextField("5");
		massInput.setLayoutX(massControlLabel.getLayoutX() + massControlLabel.getLayoutBounds().getWidth() + 10);
		massInput.setLayoutY(213);
		massInput.setMaxWidth(50);
		massInput.setAlignment(Pos.CENTER);
		massInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateParticlePreview();
			}

		});

		forcefieldControlLabel = new Text("Forcefield");
		forcefieldControlLabel.setLayoutX(
				controlPane.getPrefWidth() / 2 - forcefieldControlLabel.getLayoutBounds().getWidth() / 2 - 30);
		forcefieldControlLabel.setLayoutY(260);

		forcefieldInput = new TextField("0");
		forcefieldInput.setLayoutX(
				forcefieldControlLabel.getLayoutX() + forcefieldControlLabel.getLayoutBounds().getWidth() + 10);
		forcefieldInput.setLayoutY(243);
		forcefieldInput.setMaxWidth(50);
		forcefieldInput.setAlignment(Pos.CENTER);
		forcefieldInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateParticlePreview();
			}

		});

		colorControlLabel = new Text("Color");
		colorControlLabel
				.setLayoutX(controlPane.getPrefWidth() / 2 - colorControlLabel.getLayoutBounds().getWidth() / 2);
		colorControlLabel.setLayoutY(285);

		rControlLabel = new Text("R");
		rControlLabel.setLayoutX(controlPane.getPrefWidth() / 2 - rControlLabel.getLayoutBounds().getWidth() / 2 - 80);
		rControlLabel.setLayoutY(310);

		rInput = new TextField("255");
		rInput.setLayoutX(rControlLabel.getLayoutX() + 10);
		rInput.setLayoutY(293);
		rInput.setMaxWidth(35);
		rInput.setAlignment(Pos.CENTER);
		rInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateParticlePreview();
			}

		});

		gControlLabel = new Text("G");
		gControlLabel.setLayoutX(controlPane.getPrefWidth() / 2 - gControlLabel.getLayoutBounds().getWidth() / 2 - 20);
		gControlLabel.setLayoutY(310);

		gInput = new TextField("255");
		gInput.setLayoutX(gControlLabel.getLayoutX() + 10);
		gInput.setLayoutY(293);
		gInput.setMaxWidth(35);
		gInput.setAlignment(Pos.CENTER);
		gInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateParticlePreview();
			}

		});

		bControlLabel = new Text("B");
		bControlLabel.setLayoutX(controlPane.getPrefWidth() / 2 - bControlLabel.getLayoutBounds().getWidth() / 2 + 40);
		bControlLabel.setLayoutY(310);

		bInput = new TextField("255");
		bInput.setLayoutX(bControlLabel.getLayoutX() + 10);
		bInput.setLayoutY(293);
		bInput.setMaxWidth(35);
		bInput.setAlignment(Pos.CENTER);
		bInput.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateParticlePreview();
			}

		});

		randomColorButton = new Button("Random Color");
		randomColorButton.setLayoutX(controlPane.getPrefWidth() / 2 - 40);
		randomColorButton.setLayoutY(323);
		randomColorButton.setOnAction(e -> {
			rInput.setText("" + (int) random(0, 255));
			gInput.setText("" + (int) random(0, 255));
			bInput.setText("" + (int) random(0, 255));
		});

		specialParticleCheckBox = new CheckBox("Special Particle");
		specialParticleCheckBox.setLayoutX(controlPane.getPrefWidth() / 2 - 40);
		specialParticleCheckBox.setLayoutY(350);

		instructionControlLabel = new Text("Drag and Drop");
		instructionControlLabel
				.setLayoutX(controlPane.getPrefWidth() / 2 - instructionControlLabel.getLayoutBounds().getWidth() / 2);
		instructionControlLabel.setLayoutY(380);

		previewCircle = new Circle(20, Color.WHITE);
		previewCircle.setCenterX(controlPane.getPrefWidth() / 2);
		previewCircle.setCenterY(390 + previewCircle.getRadius());
		previewCircle.setStroke(Color.BLACK);
		// Handles drag and drop particle placement
		previewCircle.setOnMouseDragged(event -> {
			previewCircle.setCenterX(event.getX());
			previewCircle.setCenterY(event.getY());
			previewCircle.toBack();
		});
		previewCircle.setOnMouseReleased(event -> {
			previewCircle.setCenterX(controlPane.getPrefWidth() / 2);
			previewCircle.setCenterY(390 + previewCircle.getRadius());

			double radius, mass, forcefield, r, g, b;

			try {
				radius = clamp(Double.parseDouble(radiusInput.getText()), 5, controlPane.getPrefWidth() / 2);
			} catch (Exception e) {
				radius = 5;
			}
			try {
				mass = clamp(Double.parseDouble(massInput.getText()), -1000, 1000);
			} catch (Exception e) {
				mass = 20;
			}
			try {
				forcefield = clamp(Double.parseDouble(forcefieldInput.getText()), -10000, 10000);
			} catch (Exception e) {
				forcefield = 0;
			}
			try {
				r = map(Double.parseDouble(rInput.getText()), 0, 255, 0, 1);
			} catch (Exception e) {
				r = 0;
			}
			try {
				g = map(Double.parseDouble(gInput.getText()), 0, 255, 0, 1);
			} catch (Exception e) {
				g = 0;
			}
			try {
				b = map(Double.parseDouble(bInput.getText()), 0, 255, 0, 1);
			} catch (Exception e) {
				b = 0;
			}

			if (event.getX() < 0) {
				if (specialParticleCheckBox.isSelected()) {
					addSpecialParticle(nameInput.getText(), event.getSceneX(), sandbox.getHeight() - event.getSceneY(),
							radius, mass, r, g, b, forcefield);
				} else {
					addParticle(nameInput.getText(), event.getSceneX(), sandbox.getHeight() - event.getSceneY(), radius,
							mass, r, g, b, forcefield);
				}
			}
			previewCircle.setStroke(new Color(1 - r, 1 - g, 1 - b, 1));
			previewCircle.toBack();
		});

		saveLoadParticleButton = new Button("Save/Load");
		saveLoadParticleButton.setLayoutX(controlPane.getPrefWidth() / 2 - 35);
		saveLoadParticleButton.setLayoutY(440);
		saveLoadParticleButton.setOnAction(new ShowParticleSaveHandler());

		addRandomButton = new Button("Add Particle at Random Position");
		addRandomButton.setLayoutX(controlPane.getPrefWidth() / 2 - 93);
		addRandomButton.setLayoutY(470);
		addRandomButton.setOnAction(event -> {
			double radius, mass, forcefield, r, g, b;

			try {
				radius = clamp(Double.parseDouble(radiusInput.getText()), 5, controlPane.getPrefWidth() / 2);
			} catch (Exception e) {
				radius = 5;
			}
			try {
				mass = clamp(Double.parseDouble(massInput.getText()), -1000, 1000);
			} catch (Exception e) {
				mass = 20;
			}
			try {
				forcefield = clamp(Double.parseDouble(forcefieldInput.getText()), -10000, 10000);
			} catch (Exception e) {
				forcefield = 0;
			}
			try {
				r = map(Double.parseDouble(rInput.getText()), 0, 255, 0, 1);
			} catch (Exception e) {
				r = 0;
			}
			try {
				g = map(Double.parseDouble(gInput.getText()), 0, 255, 0, 1);
			} catch (Exception e) {
				g = 0;
			}
			try {
				b = map(Double.parseDouble(bInput.getText()), 0, 255, 0, 1);
			} catch (Exception e) {
				b = 0;
			}

			if (specialParticleCheckBox.isSelected()) {
				addSpecialParticle(nameInput.getText(), random(0, sandbox.getWidth()), random(0, sandbox.getHeight()),
						radius, mass, r, g, b, forcefield);
			} else {
				addParticle(nameInput.getText(), random(0, sandbox.getWidth()), random(0, sandbox.getHeight()), radius,
						mass, r, g, b, forcefield);
			}
		});

		controlPane.getChildren().addAll(environmentControlLabel, gravityControlLabel, gravityXControlLabel,
				gravityYControlLabel, customizerControlLabel, nameControlLabel, radiusControlLabel, massControlLabel,
				colorControlLabel, rControlLabel, gControlLabel, bControlLabel, forcefieldControlLabel,
				instructionControlLabel);
		controlPane.getChildren().addAll(gravityXInput, gravityYInput, nameInput, radiusInput, massInput, rInput,
				gInput, bInput, forcefieldInput, specialParticleCheckBox);
		controlPane.getChildren().addAll(flowButton, resetButton, saveLoadSandboxButton, randomColorButton,
				saveLoadParticleButton, addRandomButton);
		controlPane.getChildren().add(previewCircle);

		// END CONTROL PANE SETUP

		display.setCenter(sandbox);
		display.setRight(controlPane);
	}

	public void start(Stage primaryStage) throws Exception {

		// Set the scene and the stage
		Scene scene = new Scene(display, stageWidth, stageHeight);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Physics Sandbox");

		// Display the GUI
		primaryStage.show();
	}

	// Handles particle updating and hitbox detection
	private void simulate() {
		updateBackgroundColor();
		if (running) {
			for (int i = 0; i < specialParticles.size(); i++) {

				// checking conditions to bounce off window edges
				double futureX = specialParticles.get(i).getPosition().getX()
						+ (specialParticles.get(i).getVelocity().getX() * timeStep);
				if (futureX - specialParticles.get(i).getRadius() <= 0
						|| futureX + specialParticles.get(i).getRadius() >= sandbox.getWidth()) {
					specialParticles.get(i).bounceHorizontal();
				}
				double futureY = specialParticles.get(i).getPosition().getY()
						+ (specialParticles.get(i).getVelocity().getY() * timeStep);
				if (futureY - specialParticles.get(i).getRadius() <= 0
						|| futureY + specialParticles.get(i).getRadius() >= sandbox.getHeight()) {
					specialParticles.get(i).bounceVertical();
				}

				// keeps specialParticles from phasing through window edges
				if (specialParticles.get(i).getPosition().getY() + specialParticles.get(i).getRadius() >= sandbox
						.getHeight()) {
					specialParticles.get(i).getPosition()
							.setY(sandbox.getHeight() - specialParticles.get(i).getRadius());
				}
				if (specialParticles.get(i).getPosition().getY() - specialParticles.get(i).getRadius() <= 0) {
					specialParticles.get(i).getPosition().setY(0 + specialParticles.get(i).getRadius());
				}
				if (specialParticles.get(i).getPosition().getX() + specialParticles.get(i).getRadius() >= sandbox
						.getWidth()) {
					specialParticles.get(i).getPosition()
							.setX(sandbox.getWidth() - specialParticles.get(i).getRadius());
				}
				if (specialParticles.get(i).getPosition().getX() - specialParticles.get(i).getRadius() <= 0) {
					specialParticles.get(i).getPosition().setX(0 + specialParticles.get(i).getRadius());
				}

				// update the specialParticles positions and then update their circles'
				// locations
				specialParticles.get(i).updateAll(timeStep);
				circles.get(specialParticles.get(i).getCircleIndex())
						.setCenterX(specialParticles.get(i).getPosition().getX());
				circles.get(specialParticles.get(i).getCircleIndex())
						.setCenterY(sandbox.getHeight() - specialParticles.get(i).getPosition().getY());
				circles.get(specialParticles.get(i).getCircleIndex()).setFill(specialParticles.get(i).getColor());
			}
			for (int i = 0; i < particles.size(); i++) {

				// checking conditions to bounce off window edges
				double futureX = particles.get(i).getPosition().getX()
						+ (particles.get(i).getVelocity().getX() * timeStep);
				if (futureX - particles.get(i).getRadius() <= 0
						|| futureX + particles.get(i).getRadius() >= sandbox.getWidth()) {
					particles.get(i).bounceHorizontal();
				}
				double futureY = particles.get(i).getPosition().getY()
						+ (particles.get(i).getVelocity().getY() * timeStep);
				if (futureY - particles.get(i).getRadius() <= 0
						|| futureY + particles.get(i).getRadius() >= sandbox.getHeight()) {
					particles.get(i).bounceVertical();
				}

				// for interactions between particles
				for (int j = 0; j < particles.size(); j++) {

					if (i != j) {
						// collision detection
						double futureXi = particles.get(i).getPosition().getX()
								+ (particles.get(i).getVelocity().getX() * timeStep);
						double futureYi = particles.get(i).getPosition().getY()
								+ (particles.get(i).getVelocity().getY() * timeStep);
						Point futurePosi = new Point(futureXi, futureYi);
						double futureXj = particles.get(j).getPosition().getX()
								+ (particles.get(j).getVelocity().getX() * timeStep);
						double futureYj = particles.get(j).getPosition().getY()
								+ (particles.get(j).getVelocity().getY() * timeStep);
						Point futurePosj = new Point(futureXj, futureYj);

						if (futurePosi.calculateDistanceToPoint(futurePosj) <= particles.get(i).getRadius()
								+ particles.get(j).getRadius()) {
							particles.get(i).collideWith(particles.get(j));
						}

						// clipping detection
						double clipAmount = (particles.get(i).getRadius() + particles.get(j).getRadius()) - (particles
								.get(i).getPosition().calculateDistanceToPoint(particles.get(j).getPosition()));
						double angle = particles.get(j).getPosition()
								.calculateAngleToPoint(particles.get(i).getPosition());
						if (clipAmount > 0) {

							particles.get(i).getPosition().applyVector(new Vector(Math.cos(angle), Math.sin(angle)),
									clipAmount);

						}

						// force application
						double forceScaler = 100;
						particles.get(i)
								.applyForce(Vector.multiply(new Vector(Math.cos(angle), Math.sin(angle)),
										forceScaler * particles.get(j).getForceStrength()
												/ particles.get(i).getPosition()
														.calculateDistanceToPoint(particles.get(j).getPosition())));
					}
				}

				// keeps particles from phasing through window edges
				if (particles.get(i).getPosition().getY() + particles.get(i).getRadius() >= sandbox.getHeight()) {
					particles.get(i).getPosition().setY(sandbox.getHeight() - particles.get(i).getRadius());
				}
				if (particles.get(i).getPosition().getY() - particles.get(i).getRadius() <= 0) {
					particles.get(i).getPosition().setY(0 + particles.get(i).getRadius());
				}
				if (particles.get(i).getPosition().getX() + particles.get(i).getRadius() >= sandbox.getWidth()) {
					particles.get(i).getPosition().setX(sandbox.getWidth() - particles.get(i).getRadius());
				}
				if (particles.get(i).getPosition().getX() - particles.get(i).getRadius() <= 0) {
					particles.get(i).getPosition().setX(0 + particles.get(i).getRadius());
				}

				// update the particles positions and then update their circles' locations
				particles.get(i).updateAll(timeStep);
				circles.get(particles.get(i).getCircleIndex()).setCenterX(particles.get(i).getPosition().getX());
				circles.get(particles.get(i).getCircleIndex())
						.setCenterY(sandbox.getHeight() - particles.get(i).getPosition().getY());

			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Start simulation handler
	private class StartHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			running = true;
			flowButton.setOnAction(new StopHandler());
			flowButton.setText("Stop");
			animation.play();
		}

	}

	// Pause simulation handler
	private class StopHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			running = false;
			flowButton.setOnAction(new StartHandler());
			flowButton.setText("Play");
			animation.stop();
		}

	}

	// Reset simulation handler
	private class ResetHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			particles.clear();
			specialParticles.clear();
			sandbox.getChildren().removeAll(circles);
			circles.clear();
			display.setRight(controlPane);
			updateBackgroundColor();
			new ShowSandboxFromSandboxSaveHandler().handle(event);
			new ShowSandboxFromParticleSaveHandler().handle(event);
			new StopHandler().handle(event);
		}

	}

	// Sandbox save button handler
	private class SaveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (saveNameInput.getText().isEmpty()) {
				System.out.println("No Save Name");
				return;
			}
			saveSandbox(saveNameInput.getText());
			getSaveList();
		}

	}

	// Sandbox load button Handler
	private class LoadHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (saveList.getItems().contains(saveNameInput.getText())) {
				loadSandbox(saveNameInput.getText());
				new ShowSandboxFromSandboxSaveHandler().handle(event);
				controlPane.toFront();
				display.getChildren().remove(controlPane);
				display.setRight(controlPane);
				new StopHandler().handle(event);
			}
		}

	}

	// Handles switching to sanbox save menu
	private class ShowSandboxSaveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			display.setCenter(sandboxSavePane);
			new StopHandler().handle(event);
			getSaveList();
			saveLoadSandboxButton.setOnAction(new ShowSandboxFromSandboxSaveHandler());
			saveLoadParticleButton.setOnAction(new ShowParticleSaveHandler());
		}

	}

	// Handles switching to particle save menu
	private class ShowParticleSaveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			display.setCenter(particleSavePane);
			new StopHandler().handle(event);
			getParticleList();
			saveLoadParticleButton.setOnAction(new ShowSandboxFromParticleSaveHandler());
			saveLoadSandboxButton.setOnAction(new ShowSandboxSaveHandler());
		}

	}

	// Handles switching to sandbox from particle save menu
	private class ShowSandboxFromParticleSaveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			display.setCenter(sandbox);
			display.getChildren().remove(controlPane);
			display.setRight(controlPane);
			saveLoadParticleButton.setOnAction(new ShowParticleSaveHandler());
		}

	}

	// Handles switching to sandbox from sandbox save menu
	private class ShowSandboxFromSandboxSaveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			display.setCenter(sandbox);
			display.getChildren().remove(controlPane);
			display.setRight(controlPane);
			saveLoadSandboxButton.setOnAction(new ShowSandboxSaveHandler());
		}

	}

	// Function for adding particles
	private void addParticle(String name, double x, double y, double radius, double mass, double r, double g, double b,
			double forceStrength) {
		if (particles.size() >= 250)
			return;
		Particle newParticle = new Particle(mass, new Point(x, y), circles.size());
		newParticle.setRadius(radius);
		newParticle.setForceStrength(forceStrength);
		newParticle.setColor(r, g, b);
		newParticle.setName(name);

		Circle newCircle = new Circle();
		newCircle.setCenterX(newParticle.getPosition().getX());
		newCircle.setCenterY(sandbox.getHeight() - newParticle.getPosition().getY());
		newCircle.setRadius(newParticle.getRadius());
		newCircle.setFill(new Color(r, g, b, 1));
		newCircle.setOnMouseClicked(e -> {
			inspect(newParticle, newCircle);
		});
		circles.add(newCircle);
		sandbox.getChildren().add(newCircle);

		particles.add(newParticle);

		updateBackgroundColor();
	}

	// function for adding special particles
	private void addSpecialParticle(String name, double x, double y, double radius, double mass, double r, double g,
			double b, double forceStrength) {
		if (particles.size() >= 250)
			return;
		SpecialParticle newParticle = new SpecialParticle(mass, new Point(x, y), circles.size());
		newParticle.setRadius(radius);
		newParticle.setForceStrength(forceStrength);
		newParticle.setColor(r, g, b);
		newParticle.setName(name);

		Circle newCircle = new Circle();
		newCircle.setCenterX(newParticle.getPosition().getX());
		newCircle.setCenterY(sandbox.getHeight() - newParticle.getPosition().getY());
		newCircle.setRadius(newParticle.getRadius());
		newCircle.setFill(new Color(r, g, b, 1));
		newCircle.setOnMouseClicked(e -> {
			inspect(newParticle, newCircle);
		});
		circles.add(newCircle);
		sandbox.getChildren().add(newCircle);

		specialParticles.add(newParticle);

		updateBackgroundColor();
	}

	// Function for inspecting particles, populates inspector menu
	private void inspect(Particle p, Circle c) {

		inspectorTargetParticle = p;
		inspectorTargetCircle = c;

		nameInspectorLabel.setText("Name: " + ((Particle) inspectorTargetParticle).getName());
		nameInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - nameInspectorLabel.getLayoutBounds().getWidth() / 2);
		nameInspectorLabel.setLayoutY(60);

		radiusInspectorLabel.setText("Radius: " + (float) ((Particle) inspectorTargetParticle).getRadius());
		radiusInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - radiusInspectorLabel.getLayoutBounds().getWidth() / 2);
		radiusInspectorLabel.setLayoutY(80);

		massInspectorLabel.setText("Mass: " + inspectorTargetParticle.getMass());
		massInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - massInspectorLabel.getLayoutBounds().getWidth() / 2);
		massInspectorLabel.setLayoutY(100);

		colorInspectorLabel.setText("Color\nR: "
				+ (float) map(((Particle) inspectorTargetParticle).getColor().getRed(), 0, 1, 0, 255) + "  G: "
				+ (float) map(((Particle) inspectorTargetParticle).getColor().getGreen(), 0, 1, 0, 255) + "  B: "
				+ (float) map(((Particle) inspectorTargetParticle).getColor().getBlue(), 0, 1, 0, 255));
		colorInspectorLabel.setTextAlignment(TextAlignment.CENTER);
		colorInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - colorInspectorLabel.getLayoutBounds().getWidth() / 2);
		colorInspectorLabel.setLayoutY(120);

		forcefieldInspectorLabel.setText("Forcefield Strength: " + (float) inspectorTargetParticle.getForceStrength());
		forcefieldInspectorLabel.setLayoutX(
				inspectorPane.getPrefWidth() / 2 - forcefieldInspectorLabel.getLayoutBounds().getWidth() / 2);
		forcefieldInspectorLabel.setLayoutY(155);

		inspectorPreviewCircle.setFill(((Particle) inspectorTargetParticle).getColor());
		inspectorPreviewCircle.setRadius(((Particle) inspectorTargetParticle).getRadius());
		inspectorPreviewCircle.setCenterX(inspectorPane.getPrefWidth() / 2);
		inspectorPreviewCircle.setCenterY(200 + inspectorPreviewCircle.getRadius());

		display.setRight(inspectorPane);
	}

	// Function for inspecting special particles, populates inspector menu
	private void inspect(SpecialParticle p, Circle c) {

		inspectorTargetParticle = p;
		inspectorTargetCircle = c;

		nameInspectorLabel.setText("SP Name: " + ((SpecialParticle) inspectorTargetParticle).getName());
		nameInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - nameInspectorLabel.getLayoutBounds().getWidth() / 2);
		nameInspectorLabel.setLayoutY(60);

		radiusInspectorLabel.setText("Radius: " + (float) ((SpecialParticle) inspectorTargetParticle).getRadius());
		radiusInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - radiusInspectorLabel.getLayoutBounds().getWidth() / 2);
		radiusInspectorLabel.setLayoutY(80);

		massInspectorLabel.setText("Mass: " + inspectorTargetParticle.getMass());
		massInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - massInspectorLabel.getLayoutBounds().getWidth() / 2);
		massInspectorLabel.setLayoutY(100);

		colorInspectorLabel.setText("Color\nR: "
				+ (float) map(((SpecialParticle) inspectorTargetParticle).getColor().getRed(), 0, 1, 0, 255) + "  G: "
				+ (float) map(((SpecialParticle) inspectorTargetParticle).getColor().getGreen(), 0, 1, 0, 255) + "  B: "
				+ (float) map(((SpecialParticle) inspectorTargetParticle).getColor().getBlue(), 0, 1, 0, 255));
		colorInspectorLabel.setTextAlignment(TextAlignment.CENTER);
		colorInspectorLabel
				.setLayoutX(inspectorPane.getPrefWidth() / 2 - colorInspectorLabel.getLayoutBounds().getWidth() / 2);
		colorInspectorLabel.setLayoutY(120);

		forcefieldInspectorLabel.setText("Forcefield Strength: " + (float) inspectorTargetParticle.getForceStrength());
		forcefieldInspectorLabel.setLayoutX(
				inspectorPane.getPrefWidth() / 2 - forcefieldInspectorLabel.getLayoutBounds().getWidth() / 2);
		forcefieldInspectorLabel.setLayoutY(155);

		inspectorPreviewCircle.setFill(((SpecialParticle) inspectorTargetParticle).getColor());
		inspectorPreviewCircle.setRadius(((SpecialParticle) inspectorTargetParticle).getRadius());
		inspectorPreviewCircle.setCenterX(inspectorPane.getPrefWidth() / 2);
		inspectorPreviewCircle.setCenterY(200 + inspectorPreviewCircle.getRadius());

		display.setRight(inspectorPane);
	}

	// my random function for easier use
	private double random(double min, double max) {
		return (max - min) * Math.random() + min;
	}

	// my mapping function, used for scaling a value from one range to another
	private double map(double num, double min, double max, double newMin, double newMax) {
		num = clamp(num, min, max);
		return ((num - min) / (max - min)) * (newMax - newMin) + newMin;
	}

	// Function for adaptive background color, changes background to the opposite of
	// the average particle color
	private void updateBackgroundColor() {
		double rSum = 0;
		double gSum = 0;
		double bSum = 0;

		for (Particle p : particles) {
			rSum += p.getColor().getRed();
			gSum += p.getColor().getGreen();
			bSum += p.getColor().getBlue();
		}
		
		for (SpecialParticle sp : specialParticles) {
			rSum += sp.getColor().getRed();
			gSum += sp.getColor().getGreen();
			bSum += sp.getColor().getBlue();
		}
		
		double denominator = particles.size() + specialParticles.size();
		if (denominator == 0)
			denominator = 1;
		
		double rAvg = rSum / denominator;
		double gAvg = gSum / denominator;
		double bAvg = bSum / denominator;
		
		sandboxBackgroundColor = new Color(1 - rAvg, 1 - gAvg, 1 - bAvg, 1);

		sandbox.setBackground(new Background(new BackgroundFill(sandboxBackgroundColor, null, null)));
	}

	// Function for updating drag and drop particle's visual based on text boxes
	private void updateParticlePreview() {
		double radius, r, g, b;

		try {
			radius = clamp(Double.parseDouble(radiusInput.getText()), 5, controlPane.getPrefWidth() / 2);
		} catch (Exception e) {
			radius = 5;
		}
		previewCircle.setRadius(radius);

		try {
			r = map(Double.parseDouble(rInput.getText()), 0, 255, 0, 1);
		} catch (Exception e) {
			r = 0;
		}
		try {
			g = map(Double.parseDouble(gInput.getText()), 0, 255, 0, 1);
		} catch (Exception e) {
			g = 0;
		}
		try {
			b = map(Double.parseDouble(bInput.getText()), 0, 255, 0, 1);
		} catch (Exception e) {
			b = 0;
		}
		previewCircle.setFill(new Color(r, g, b, 1));
		previewCircle.setStroke(new Color(1 - r, 1 - g, 1 - b, 1));

		previewCircle.setCenterY(390 + previewCircle.getRadius());
	}

	// Function for updating simulation gravity based on text boxes
	private void updateGravity() {
		double x, y;
		try {
			x = clamp(Double.parseDouble(gravityXInput.getText()), -10000, 10000);
		} catch (Exception e) {
			x = 0;
		}
		try {
			y = clamp(Double.parseDouble(gravityYInput.getText()), -10000, 10000);
		} catch (Exception e) {
			y = 0;
		}
		gravity.setX(x);
		gravity.setY(y);
	}

	// My clamping function for convenience, keeps a value between a minimum and
	// maximum
	private double clamp(double num, double min, double max) {
		if (num < min)
			return min;
		if (num > max)
			return max;
		return num;
	}

	// Save function for sandbox state
	private void saveSandbox(String saveName) {
		try {
			FileWriter fw = new FileWriter(saveName + "SandboxSave.txt");
			BufferedWriter bw = new BufferedWriter(fw);

			String toWrite = "" + gravity.getX() + "," + gravity.getY();
			bw.write(toWrite);
			bw.newLine();
			for (Particle p : particles) {
				toWrite = "" + p.getPosition().getX() + "," + p.getPosition().getY() + "," + p.getVelocity().getX()
						+ "," + p.getVelocity().getY() + "," + p.getAcceleration().getX() + ","
						+ p.getAcceleration().getY() + "," + p.getMass() + "," + p.getRadius() + ","
						+ p.getForceStrength() + "," + p.getColor().getRed() + "," + p.getColor().getGreen() + ","
						+ p.getColor().getBlue() + "," + p.getName() + ",false";
				bw.write(toWrite);
				bw.newLine();
			}

			for (SpecialParticle p : specialParticles) {
				toWrite = "" + p.getPosition().getX() + "," + p.getPosition().getY() + "," + p.getVelocity().getX()
						+ "," + p.getVelocity().getY() + "," + p.getAcceleration().getX() + ","
						+ p.getAcceleration().getY() + "," + p.getMass() + "," + p.getRadius() + ","
						+ p.getForceStrength() + "," + p.getColor().getRed() + "," + p.getColor().getGreen() + ","
						+ p.getColor().getBlue() + "," + p.getName() + ",true";
				bw.write(toWrite);
				bw.newLine();
			}

			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Save Failed");
		}
	}

	// Load function for sandbox state
	private void loadSandbox(String saveName) {
		try {
			FileReader fr = new FileReader(saveName + "SandboxSave.txt");
			BufferedReader br = new BufferedReader(fr);

			String currentLine = br.readLine();
			String[] parameters = currentLine.split(",");

			gravityXInput.setText(parameters[0]);
			gravityYInput.setText(parameters[1]);

			updateGravity();

			particles.clear();
			sandbox.getChildren().removeAll(circles);
			circles.clear();
			while ((currentLine = br.readLine()) != null) {
				parameters = currentLine.split(",");
				if (parameters[13].equals("true")) {
					SpecialParticle newParticle = new SpecialParticle(Double.parseDouble(parameters[6]),
							new Point(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1])),
							circles.size());
					newParticle.setVelocity(
							new Vector(Double.parseDouble(parameters[2]), Double.parseDouble(parameters[3])));
					newParticle.setAcceleration(
							new Vector(Double.parseDouble(parameters[4]), Double.parseDouble(parameters[5])));
					(newParticle).setRadius(Double.parseDouble(parameters[7]));
					newParticle.setForceStrength(Double.parseDouble(parameters[8]));
					newParticle.setColor(Double.parseDouble(parameters[9]), Double.parseDouble(parameters[10]),
							Double.parseDouble(parameters[11]));
					newParticle.setName(parameters[12]);
					specialParticles.add(newParticle);
					Circle newCircle = new Circle(newParticle.getPosition().getX(),
							sandbox.getHeight() - newParticle.getPosition().getY(), newParticle.getRadius());
					newCircle.setFill(newParticle.getColor());
					newCircle.setOnMouseClicked(e -> {
						inspect(newParticle, newCircle);
					});

					circles.add(newCircle);
				} else {
					Particle newParticle = new Particle(Double.parseDouble(parameters[6]),
							new Point(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1])),
							circles.size());
					newParticle.setVelocity(
							new Vector(Double.parseDouble(parameters[2]), Double.parseDouble(parameters[3])));
					newParticle.setAcceleration(
							new Vector(Double.parseDouble(parameters[4]), Double.parseDouble(parameters[5])));
					(newParticle).setRadius(Double.parseDouble(parameters[7]));
					newParticle.setForceStrength(Double.parseDouble(parameters[8]));
					newParticle.setColor(Double.parseDouble(parameters[9]), Double.parseDouble(parameters[10]),
							Double.parseDouble(parameters[11]));
					newParticle.setName(parameters[12]);
					particles.add(newParticle);
					Circle newCircle = new Circle(newParticle.getPosition().getX(),
							sandbox.getHeight() - newParticle.getPosition().getY(), newParticle.getRadius());
					newCircle.setFill(newParticle.getColor());
					newCircle.setOnMouseClicked(e -> {
						inspect(newParticle, newCircle);
					});

					circles.add(newCircle);
				}
			}
			sandbox.getChildren().addAll(circles);
			updateBackgroundColor();
			br.close();
			fr.close();
		} catch (IOException e) {
			System.out.println("Load Failed");
		}
	}

	// Populates sandbox save ListView with save files
	private void getSaveList() {
		try {
			File directoryPath = new File(System.getProperty("user.dir"));
			File[] allFiles = directoryPath.listFiles();
			ArrayList<File> saves = new ArrayList<File>();
			ObservableList<String> listItems = saveList.getItems();
			listItems.clear();

			for (File f : allFiles) {
				if (f.getName().length() >= 15
						&& f.getName().substring(f.getName().length() - 15).equals("SandboxSave.txt")) {
					saves.add(f);
					listItems.add(f.getName().substring(0, f.getName().length() - 15));
				}
			}

		} catch (Exception e) {
			System.out.println("Problem Loading Sandbox Saves");
			e.printStackTrace();
		}
	}

	// Populates particle save ListView with saved particles
	private void getParticleList() {
		try {
			File particleSaves = new File("particleSaves.txt");
			particleSaves.createNewFile();

			FileReader fr = new FileReader(particleSaves);
			BufferedReader br = new BufferedReader(fr);

			String currentLine;
			String[] parameters;

			ObservableList<String> listItems = particleSaveList.getItems();
			listItems.clear();

			while ((currentLine = br.readLine()) != null) {
				parameters = currentLine.split(",");

				listItems.add(parameters[12]);
			}

			br.close();
		} catch (Exception e) {
			System.out.println("Problem Loading Particle Saves");
			e.printStackTrace();
		}
	}
}
