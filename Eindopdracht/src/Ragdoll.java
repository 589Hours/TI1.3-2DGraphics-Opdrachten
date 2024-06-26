import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.*;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ragdoll implements GameObject {
    private World world;
    private Vector2 offset;
    private double scale;
    private String[] imagePaths;
    private BufferedImage[] images;
    private Map<Body, Integer> bodyParts = new HashMap<>();
    private ArrayList<Joint> joints = new ArrayList<>();

    public Ragdoll(World world, Vector2 offset, double scale){
        this.world = world;
        this.offset = offset;
        this.scale = scale;
        imagePaths = new String[]{
                "chest.png", "head.png",
                "upperArmL.png", "underArmL.png",
                "upperArmR.png", "underArmR.png",
                "upperLegL.png", "underLegL.png",
                "upperLegR.png", "underLegR.png"
        };
        this.images = new BufferedImage[10];
        spawn();
        prepareImages();
        setDensityForRagdoll(10);
    }

    private void prepareImages() {
        for (int i = 0; i < imagePaths.length; i++) {
            try{
                BufferedImage image = ImageIO.read(getClass().getResource("ragdoll/" + imagePaths[i]));
                images[i] = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //draw the ragdoll from Johan

    @Override
    public void draw(FXGraphics2D g2d) {
        for (Body body : bodyParts.keySet()) {
            int value = bodyParts.get(body);
            if (value < 0)
                continue;

            BufferedImage image = images[value];

            if (image == null) {
                return;
            }

            AffineTransform tx = new AffineTransform();
            tx.translate(body.getTransform().getTranslationX() * 100, body.getTransform().getTranslationY() * 100);
            tx.rotate(body.getTransform().getRotation());
            tx.scale(scale, -scale);
            tx.translate(offset.x, offset.y);

            tx.translate(-image.getWidth() / 2, -image.getHeight() / 2);
            g2d.drawImage(image, tx, null);
        }



    }
    public void spawn() {
        // Head
        Body head = new Body();
        head.setUserData("ragdoll");
        {// Fixture2
            Convex c = Geometry.createCircle(0.25);
            BodyFixture bf = new BodyFixture(c);
            head.addFixture(bf);
        }
        head.setMass(MassType.NORMAL);
        world.addBody(head);

        bodyParts.put(head, 1);

        // Torso
        Body torso = new Body();
        torso.setUserData("ragdoll");
        {// Fixture4
            Convex c = Geometry.createRectangle(0.5, 1.0);
            BodyFixture bf = new BodyFixture(c);
            torso.addFixture(bf);
        }
        {// Fixture16
            Convex c = Geometry.createRectangle(1.0, 0.25);
            c.translate(new Vector2(0.00390625, 0.375));
            BodyFixture bf = new BodyFixture(c);
            torso.addFixture(bf);
        }
        torso.translate(new Vector2(0.0234375, -0.8125));
        torso.setMass(MassType.NORMAL);
        world.addBody(torso);

        bodyParts.put(torso, 0);

        // Right Humerus
        Body rightHumerus = new Body();
        rightHumerus.setUserData("ragdoll");
        {// Fixture5
            Convex c = Geometry.createRectangle(0.25, 0.5);
            BodyFixture bf = new BodyFixture(c);
            rightHumerus.addFixture(bf);
        }
        rightHumerus.translate(new Vector2(0.4375, -0.609375));
        rightHumerus.setMass(MassType.NORMAL);
        world.addBody(rightHumerus);

        bodyParts.put(rightHumerus, 4);

        // Right Ulna
        Body rightUlna = new Body();
        rightUlna.setUserData("ragdoll");
        {// Fixture6
            Convex c = Geometry.createRectangle(0.25, 0.4);
            BodyFixture bf = new BodyFixture(c);
            rightUlna.addFixture(bf);
        }
        rightUlna.translate(new Vector2(0.44140625, -0.98828125));
        rightUlna.setMass(MassType.NORMAL);
        world.addBody(rightUlna);

        bodyParts.put(rightUlna, 5);

        // Neck
        Body neck = new Body();
        neck.setUserData("ragdoll");
        {// Fixture7
            Convex c = Geometry.createRectangle(0.15, 0.2);
            BodyFixture bf = new BodyFixture(c);
            neck.addFixture(bf);
        }
        neck.translate(new Vector2(0.015625, -0.2734375));
        neck.setMass(MassType.NORMAL);
        world.addBody(neck);

        bodyParts.put(neck, -1);

        // Left Humerus
        Body leftHumerus = new Body();
        leftHumerus.setUserData("ragdoll");
        {// Fixture9
            Convex c = Geometry.createRectangle(0.25, 0.5);
            BodyFixture bf = new BodyFixture(c);
            leftHumerus.addFixture(bf);
        }
        leftHumerus.translate(new Vector2(-0.3828125, -0.609375));
        leftHumerus.setMass(MassType.NORMAL);
        world.addBody(leftHumerus);

        bodyParts.put(leftHumerus, 2);

        // Left Ulna
        Body leftUlna = new Body();
        leftUlna.setUserData("ragdoll");
        {// Fixture11
            Convex c = Geometry.createRectangle(0.25, 0.4);
            BodyFixture bf = new BodyFixture(c);
            leftUlna.addFixture(bf);
        }
        leftUlna.translate(new Vector2(-0.3828125, -0.9765625));
        leftUlna.setMass(MassType.NORMAL);
        world.addBody(leftUlna);

        bodyParts.put(leftUlna, 3);

        // Right Femur
        Body rightFemur = new Body();
        rightFemur.setUserData("ragdoll");
        {// Fixture12
            Convex c = Geometry.createRectangle(0.25, 0.75);
            BodyFixture bf = new BodyFixture(c);
            rightFemur.addFixture(bf);
        }
        rightFemur.translate(new Vector2(0.1796875, -1.5703125));
        rightFemur.setMass(MassType.NORMAL);
        world.addBody(rightFemur);

        bodyParts.put(rightFemur, 8);

        // Left Femur
        Body leftFemur = new Body();
        leftFemur.setUserData("ragdoll");
        {// Fixture13
            Convex c = Geometry.createRectangle(0.25, 0.75);
            BodyFixture bf = new BodyFixture(c);
            leftFemur.addFixture(bf);
        }
        leftFemur.translate(new Vector2(-0.1328125, -1.5703125));
        leftFemur.setMass(MassType.NORMAL);
        world.addBody(leftFemur);

        bodyParts.put(leftFemur, 6);

        // Right Tibia
        Body rightTibia = new Body();
        rightTibia.setUserData("ragdoll");
        {// Fixture14
            Convex c = Geometry.createRectangle(0.25, 0.5);
            BodyFixture bf = new BodyFixture(c);
            rightTibia.addFixture(bf);
        }
        rightTibia.translate(new Vector2(0.18359375, -2.11328125));
        rightTibia.setMass(MassType.NORMAL);
        world.addBody(rightTibia);

        bodyParts.put(rightTibia, 9);

        // Left Tibia
        Body leftTibia = new Body();
        leftTibia.setUserData("ragdoll");
        {// Fixture15
            Convex c = Geometry.createRectangle(0.25, 0.5);
            BodyFixture bf = new BodyFixture(c);
            leftTibia.addFixture(bf);
        }
        leftTibia.translate(new Vector2(-0.1328125, -2.1171875));
        leftTibia.setMass(MassType.NORMAL);
        world.addBody(leftTibia);

        bodyParts.put(leftTibia, 7);

        // Head to Neck
        RevoluteJoint headToNeck = new RevoluteJoint(head, neck, new Vector2(0.01, -0.2));
        headToNeck.setLimitEnabled(false);
        headToNeck.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        headToNeck.setReferenceAngle(Math.toRadians(0.0));
        headToNeck.setMotorEnabled(false);
        headToNeck.setMotorSpeed(Math.toRadians(0.0));
        headToNeck.setMaximumMotorTorque(0.0);
        headToNeck.setCollisionAllowed(false);
        world.addJoint(headToNeck);

        joints.add(headToNeck);

        // Neck to Torso
        RevoluteJoint neckToTorso = new RevoluteJoint(neck, torso, new Vector2(0.01, -0.35));
        neckToTorso.setLimitEnabled(false);
        neckToTorso.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        neckToTorso.setReferenceAngle(Math.toRadians(0.0));
        neckToTorso.setMotorEnabled(false);
        neckToTorso.setMotorSpeed(Math.toRadians(0.0));
        neckToTorso.setMaximumMotorTorque(0.0);
        neckToTorso.setCollisionAllowed(false);
        world.addJoint(neckToTorso);

        joints.add(neckToTorso);

        // Torso to Left Humerus
        RevoluteJoint torsoToLeftHumerus = new RevoluteJoint(torso, leftHumerus, new Vector2(-0.4, -0.4));
        torsoToLeftHumerus.setLimitEnabled(false);
        torsoToLeftHumerus.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        torsoToLeftHumerus.setReferenceAngle(Math.toRadians(0.0));
        torsoToLeftHumerus.setMotorEnabled(false);
        torsoToLeftHumerus.setMotorSpeed(Math.toRadians(0.0));
        torsoToLeftHumerus.setMaximumMotorTorque(0.0);
        torsoToLeftHumerus.setCollisionAllowed(false);
        world.addJoint(torsoToLeftHumerus);

        joints.add(torsoToLeftHumerus);

        // Torso to Right Humerus
        RevoluteJoint torsoToRightHumerus = new RevoluteJoint(torso, rightHumerus, new Vector2(0.4, -0.4));
        torsoToRightHumerus.setLimitEnabled(false);
        torsoToRightHumerus.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        torsoToRightHumerus.setReferenceAngle(Math.toRadians(0.0));
        torsoToRightHumerus.setMotorEnabled(false);
        torsoToRightHumerus.setMotorSpeed(Math.toRadians(0.0));
        torsoToRightHumerus.setMaximumMotorTorque(0.0);
        torsoToRightHumerus.setCollisionAllowed(false);
        world.addJoint(torsoToRightHumerus);

        joints.add(torsoToRightHumerus);

        // Right Humerus to Right Ulna
        RevoluteJoint rightHumerusToRightUlna = new RevoluteJoint(rightHumerus, rightUlna, new Vector2(0.43, -0.82));
        rightHumerusToRightUlna.setLimitEnabled(false);
        rightHumerusToRightUlna.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        rightHumerusToRightUlna.setReferenceAngle(Math.toRadians(0.0));
        rightHumerusToRightUlna.setMotorEnabled(false);
        rightHumerusToRightUlna.setMotorSpeed(Math.toRadians(0.0));
        rightHumerusToRightUlna.setMaximumMotorTorque(0.0);
        rightHumerusToRightUlna.setCollisionAllowed(false);
        world.addJoint(rightHumerusToRightUlna);

        joints.add(rightHumerusToRightUlna);

        // Left Humerus to Left Ulna
        RevoluteJoint leftHumerusToLeftUlna = new RevoluteJoint(leftHumerus, leftUlna, new Vector2(-0.4, -0.81));
        leftHumerusToLeftUlna.setLimitEnabled(false);
        leftHumerusToLeftUlna.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        leftHumerusToLeftUlna.setReferenceAngle(Math.toRadians(0.0));
        leftHumerusToLeftUlna.setMotorEnabled(false);
        leftHumerusToLeftUlna.setMotorSpeed(Math.toRadians(0.0));
        leftHumerusToLeftUlna.setMaximumMotorTorque(0.0);
        leftHumerusToLeftUlna.setCollisionAllowed(false);
        world.addJoint(leftHumerusToLeftUlna);

        joints.add(leftHumerusToLeftUlna);

        // Torso to Right Femur
        RevoluteJoint torsoToRightFemur = new RevoluteJoint(torso, rightFemur, new Vector2(0.16, -1.25));
        torsoToRightFemur.setLimitEnabled(false);
        torsoToRightFemur.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        torsoToRightFemur.setReferenceAngle(Math.toRadians(0.0));
        torsoToRightFemur.setMotorEnabled(false);
        torsoToRightFemur.setMotorSpeed(Math.toRadians(0.0));
        torsoToRightFemur.setMaximumMotorTorque(0.0);
        torsoToRightFemur.setCollisionAllowed(false);
        world.addJoint(torsoToRightFemur);

        joints.add(torsoToRightFemur);

        // Torso to Left Femur
        RevoluteJoint torsoToLeftFemur = new RevoluteJoint(torso, leftFemur, new Vector2(-0.13, -1.25));
        torsoToLeftFemur.setLimitEnabled(false);
        torsoToLeftFemur.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        torsoToLeftFemur.setReferenceAngle(Math.toRadians(0.0));
        torsoToLeftFemur.setMotorEnabled(false);
        torsoToLeftFemur.setMotorSpeed(Math.toRadians(0.0));
        torsoToLeftFemur.setMaximumMotorTorque(0.0);
        torsoToLeftFemur.setCollisionAllowed(false);
        world.addJoint(torsoToLeftFemur);

        joints.add(torsoToLeftFemur);

        // Right Femur to Right Tibia
        RevoluteJoint rightFemurToRightTibia = new RevoluteJoint(rightFemur, rightTibia, new Vector2(0.17, -1.9));
        rightFemurToRightTibia.setLimitEnabled(false);
        rightFemurToRightTibia.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        rightFemurToRightTibia.setReferenceAngle(Math.toRadians(0.0));
        rightFemurToRightTibia.setMotorEnabled(false);
        rightFemurToRightTibia.setMotorSpeed(Math.toRadians(0.0));
        rightFemurToRightTibia.setMaximumMotorTorque(0.0);
        rightFemurToRightTibia.setCollisionAllowed(false);
        world.addJoint(rightFemurToRightTibia);

        joints.add(rightFemurToRightTibia);

        // Left Femur to Left Tibia
        RevoluteJoint leftFemurToLeftTibia = new RevoluteJoint(leftFemur, leftTibia, new Vector2(-0.14, -1.9));
        leftFemurToLeftTibia.setLimitEnabled(false);
        leftFemurToLeftTibia.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
        leftFemurToLeftTibia.setReferenceAngle(Math.toRadians(0.0));
        leftFemurToLeftTibia.setMotorEnabled(false);
        leftFemurToLeftTibia.setMotorSpeed(Math.toRadians(0.0));
        leftFemurToLeftTibia.setMaximumMotorTorque(0.0);
        leftFemurToLeftTibia.setCollisionAllowed(false);
        world.addJoint(leftFemurToLeftTibia);

        joints.add(leftFemurToLeftTibia);
    }

    private void setDensityForRagdoll(int density) {
        for (Body body : bodyParts.keySet()) {
            body.getFixture(0).setDensity(density);
        }
    }

    public boolean checkIfDelete(Vector2 clickPosition){
        for (Body bodyPart : this.bodyParts.keySet()) {
            if(bodyPart.contains(clickPosition)){
                return true;
            }
        }
        return false;
    }
    public void deleteFromWorld() {
        System.out.println("going to delete!");
        for (Body bodyPart : bodyParts.keySet()) {
            System.out.println("Body part being deleted!");
            world.removeBody(bodyPart);
        }
        for (Joint joint : joints) {
            world.removeJoint(joint);
        }
        bodyParts.clear();
        joints.clear();
    }
}
