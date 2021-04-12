package org.firstinspires.ftc.teamcode.drive.opmode.driver;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.tank.Robot;

@Config
@TeleOp(group = "driver")
public class LinearDriverMode extends LinearOpMode {
    private Robot robot = null;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap);
        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()){
            //Practic baietii nostri au exact functia noastra de calculat vitezele
            robot.drive.setDrivePower(new Pose2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x));

            //SIST WOBBLE GOAL
            //inchis/deschis gheara wobble goal
            if (gamepad1.a) {
                robot.wobbleArm.clawToggle(true);
            } else if (gamepad1.b) {
                robot.wobbleArm.clawToggle(false);
            }

            //oprit sau pornit constraints
            if (gamepad1.x) {
                robot.wobbleArm.setConstraints(!robot.wobbleArm.getConstraints());
            }

            //test to position
            if (gamepad1.right_bumper) {
                robot.wobbleArm.armPositionToggleAsync(false);
            } else if (gamepad1.left_bumper) {
                robot.wobbleArm.armPositionToggleAsync(true);
            }
            //miscat brat wobble goal sus jos
            else if (gamepad1.left_trigger > 0.1 || gamepad1.right_trigger > 0.1) {
                telemetry.addData("Apasam pe triggere", "");
                if (robot.wobbleArm.getMotorMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                    robot.wobbleArm.setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
                if (gamepad1.left_trigger > 0)
                    robot.wobbleArm.moveArm(gamepad1.left_trigger);
                else if (gamepad1.right_trigger > 0)
                    robot.wobbleArm.moveArm(-gamepad1.right_trigger);
            } else if (gamepad1.dpad_up){
                robot.wobbleArm.armPositionToggle(false, 0.3);
                robot.wobbleArm.clawToggle(false);
                sleep(500);
                robot.wobbleArm.clawToggle(true);
                robot.wobbleArm.armPositionToggle(true, 0.3);
            } else if (robot.wobbleArm.getMotorMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                robot.wobbleArm.stop();
            }

            //SIST ARUNCARE
            if (gamepad2.dpad_up){
                robot.thrower.rotateAtSpeedAsync(2800);
            } else if (gamepad2.dpad_down){
                robot.thrower.stop();
            }

            if(gamepad2.dpad_right){
                robot.thrower.pushRing(true);
                robot.thrower.pushRing(false);
            }

            //SIST COLECTARE
            //TODO decomentat cand aveti constraints
            //oprit sau pornit constraints
//        if(gamepad2.x){
//            robot.collector.setConstraints(!robot.wobbleArm.getConstraints());
//        }

            //test to position
            //TODO decomentat astea cand avem constrainturi
//        if (gamepad2.right_bumper){
//            robot.collector.collectToggleAsync(false);
//        }
//        else if(gamepad2.left_bumper){
//            robot.collector.collectToggleAsync(true);
//        }
//        //miscat brat colecatare sus jos
//        else
            if (gamepad2.left_trigger > 0.1 || gamepad2.right_trigger > 0.1) {
                telemetry.addData("Apasam pe triggere", "");
                if(robot.collector.getMotorMode() == DcMotor.RunMode.RUN_TO_POSITION){
                    robot.collector.setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
                if(gamepad2.left_trigger > 0)
                    robot.collector.moveArm(gamepad2.left_trigger);
                else if(gamepad2.right_trigger > 0)
                    robot.collector.moveArm(-gamepad2.right_trigger);
            }
            else if(robot.collector.getMotorMode() == DcMotor.RunMode.RUN_USING_ENCODER){
                robot.collector.stop();
            }

            if (gamepad2.y){
                robot.collector.holdRingToggle(true);
            } else if (gamepad2.x){
                robot.collector.holdRingToggle(false);
            }

            if(gamepad2.a && gamepad2.b){
                robot.collector.collectToggle(false);
                robot.collector.holdRingToggle(false);
//                robot.thrower.rotateAtSpeedAsync(2800);
                robot.collector.collectToggle(true);
            }

            //updates
            if(!Thread.currentThread().isInterrupted()){
                robot.wobbleArm.updateSub();
                robot.collector.updateSub();
            }

            //TELEMETRIES
            //telemetry.addData("encoder motor colectare: ", robot.collector.getPosition());

        telemetry.addData("pozitie brat: ", robot.wobbleArm.getPosition());
        telemetry.addData("constraints: ", robot.wobbleArm.getConstraints());
        telemetry.addData("motor mode: ", robot.wobbleArm.getMotorMode());
        telemetry.addData("motor.isBUsy(): ", robot.wobbleArm.getMotorIsBusy());
        telemetry.addData("subsystem mode", robot.wobbleArm.getMode());
        telemetry.addLine();
        telemetry.addData("servo pos", robot.wobbleArm.getServoPosition());
//
//        if(robot.wobbleArm.getMotorMode() == DcMotor.RunMode.RUN_TO_POSITION){
//            telemetry.addData("target", robot.wobbleArm.getTargetPosition());
//        }

//            telemetry.addData("ticks/rev (mid, right): ", robot.localizer.getTicksPerRev());
//            telemetry.addData("ticks: ", robot.localizer.getTicks());

//            telemetry.addData("Thrower current", robot.thrower.getCurrent());
//            telemetry.addData("Thrower speed", robot.thrower.getVelo());

            telemetry.update();
        }

    }
}
