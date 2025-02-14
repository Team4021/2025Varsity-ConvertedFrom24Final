package frc.robot.subsystems;

import com.ctre.phoenix6.configs.ClosedLoopGeneralConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ManipConstants;

public class ShooterSubsystem extends SubsystemBase {

    DutyCycleEncoder m_absolute = new DutyCycleEncoder(1);
    SparkMax m_shootMotor = new SparkMax(ManipConstants.kShootMotor, MotorType.kBrushless);
    SparkMax m_shootMotor2 = new SparkMax(33, MotorType.kBrushless);
    SparkMaxConfig m_sparkMaxShooterMotorConfig = new SparkMaxConfig();
    SparkMaxConfig m_sparkMaxShooterMotor2Config = new SparkMaxConfig();
    TalonFX m_angleMotor = new TalonFX(ManipConstants.kWristMotor);
    PositionDutyCycle m_request;
    SoftwareLimitSwitchConfigs m_angleLimitConfigs = new SoftwareLimitSwitchConfigs();
    FeedbackConfigs m_angleFeedbackConfigs = new FeedbackConfigs();
    Slot0Configs m_anglePID = new Slot0Configs();
    TalonFXConfigurator m_angleConfig = m_angleMotor.getConfigurator();
    
    // private SparkPIDController m_shootPIDController;

    public void configShootMotor(){
        //m_shootMotor.restoreFactoryDefaults();
        //m_shootMotor2.restoreFactoryDefaults();
        //m_shootMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);
        //m_shootMotor2.setIdleMode(CANSparkBase.IdleMode.kBrake);
        // m_shootPIDController = m_shootMotor.getPIDController();
        // m_shootPIDController.setP(ManipConstants.kShootP);
        // m_shootPIDController.setI(ManipConstants.kShootI);
        // m_shootPIDController.setD(ManipConstants.kShootD);
        // m_shootPIDController.setFF(ManipConstants.kShootFF);
        // m_shootPIDController.setOutputRange(ManipConstants.kShootMinOutput, ManipConstants.kShootMaxOutput);
        //m_shootMotor.burnFlash();
        //m_shootMotor2.burnFlash();

      // setup config for m_intermediate
        m_sparkMaxShooterMotorConfig
            .inverted(true)
            //.smartCurrentLimit(ModuleConstants.kDrivingMotorCurrentLimit)
            .idleMode(IdleMode.kBrake);

        // setup config for m_intermediate2
        m_sparkMaxShooterMotor2Config
            .inverted(true)
            //.smartCurrentLimit(ModuleConstants.kDrivingMotorCurrentLimit)
            .idleMode(IdleMode.kBrake);
        
        m_shootMotor.configure(m_sparkMaxShooterMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_shootMotor2.configure(m_sparkMaxShooterMotor2Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


        m_angleLimitConfigs.withForwardSoftLimitEnable(true);
        m_angleLimitConfigs.withReverseSoftLimitEnable(true);
        m_angleLimitConfigs.withForwardSoftLimitThreshold(0.83);
        m_angleLimitConfigs.withReverseSoftLimitThreshold(0.66);
        m_angleFeedbackConfigs.withSensorToMechanismRatio(256);
        m_anglePID.kP = 4;

        m_angleConfig.apply(m_anglePID);
        m_angleConfig.apply(m_angleLimitConfigs);
        m_angleConfig.apply(m_angleFeedbackConfigs);
    }
    public void noRunshoot(){
        // m_shootPIDController.setReference(0, ControlType.kVelocity);
        m_shootMotor.set(0);
        m_shootMotor2.set(0);
        m_angleMotor.stopMotor();
        SmartDashboard.putNumber("absoluteangle", m_absolute.get());
        SmartDashboard.putNumber("angle",m_angleMotor.getPosition().getValueAsDouble());
    }
    public void runAmp(){
        // m_shootPIDController.setReference(0.5, ControlType.kVelocity);
    }
    public void runSpeaker(){
        // m_shootPIDController.setReference(0, ControlType.kVelocity);
    }
    public void reverseShoot(){
        // m_shootPIDController.setReference(-0.5, ControlType.kVelocity);
    }
    public void shoot(double speed){
        m_shootMotor.set(-speed);
        m_shootMotor2.set(speed);
    }
    public void angleShooterClose(){
        m_request = new PositionDutyCycle(0.6466);
        m_angleMotor.setControl(m_request);
    }
    public void angleShooterFar(){
        m_request = new PositionDutyCycle(0.8366);
        m_angleMotor.setControl(m_request);
    }
    public void resetToAbsolutePosition(){
        m_angleMotor.setPosition(m_absolute.get(), 0.5);
    }
  
}
