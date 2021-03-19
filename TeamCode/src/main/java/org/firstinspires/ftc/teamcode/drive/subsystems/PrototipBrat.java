package org.firstinspires.ftc.teamcode.drive.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PrototipBrat {
    public DcMotor motorBrat;
    private Servo servoBrat;
    public int lowConstraint = -370, highConstraint = -1400;
    private boolean isBusy;
    private boolean isConstraints;

    public PrototipBrat(HardwareMap hardwareMap) {
        motorBrat = hardwareMap.dcMotor.get("motorBrat");
        servoBrat = hardwareMap.servo.get("servoBrat");

        motorBrat.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBrat.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBrat.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        servoBrat.setPosition(1);
    }

    public void setConstraints(boolean constraints){
        this.isConstraints = constraints;
    }

    public boolean getConstraints(){
        return isConstraints;
    }

    public void liftArm(double speed) {
        if (isConstraints){
            if(motorBrat.getCurrentPosition()>highConstraint){
                stop();
            } else {
                motorBrat.setPower(Math.min(speed, 0.5));
            }
        } else {
            motorBrat.setPower(Math.min(speed, 0.5));
        }

    }


    public void lowerArm(double speed) {
        if(isConstraints){
            if(motorBrat.getCurrentPosition()<lowConstraint){
                stop();
            } else {
                motorBrat.setPower(Math.max(-speed, -0.3));
            }
        } else {
            motorBrat.setPower(Math.max(-speed, -0.3));
        }

    }

    public void stop() {
        motorBrat.setPower(0);
    }

    public void raiseClaw() {
        if (servoBrat.getPosition() == 0.8) {
            servoBrat.setPosition(0);
        } else if (servoBrat.getPosition() == 0) {
            servoBrat.setPosition(0.8);
        }
    }

    public void raiseClaw(boolean clamped) {
        if (!clamped) {
            servoBrat.setPosition(0);
        } else if (clamped) {
            servoBrat.setPosition(0.8);
        }
    }

    public boolean getIsBusy(){
      return isBusy;
    }

    public void toPosition(int position){
        motorBrat.setPower(0.4);
        motorBrat.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBrat.setTargetPosition(position);
        while(motorBrat.isBusy()){
            isBusy = true;
        }
        motorBrat.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        stop();
    }

    public void encoderMode(){
        motorBrat.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
