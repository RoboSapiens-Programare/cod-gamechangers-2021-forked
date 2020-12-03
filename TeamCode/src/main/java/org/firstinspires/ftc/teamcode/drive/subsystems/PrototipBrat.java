package org.firstinspires.ftc.teamcode.drive.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PrototipBrat {
    private DcMotor motorBrat;
    private Servo servoBrat;

    public PrototipBrat(HardwareMap hardwareMap){
        motorBrat = hardwareMap.dcMotor.get("motorBrat");
        servoBrat = hardwareMap.servo.get("servoBrat");

        motorBrat.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBrat.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBrat.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        servoBrat.setPosition(1);
    }

    public void moveForward(double speed){
        motorBrat.setPower(Math.min(speed, 0.11));
    }

    public void moveBackward(double speed){
        motorBrat.setPower(Math.max(-speed, -0.2));
    }

    public void stop(){
        motorBrat.setPower(0);
    }

    public void raiseClaw(){
        if(servoBrat.getPosition() == 1){
            servoBrat.setPosition(0);
        }
        else if(servoBrat.getPosition() == 0){
            servoBrat.setPosition(1);
        }
    }

}