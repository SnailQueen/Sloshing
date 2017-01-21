
public class EquationSloshing {
    //this parameter is not configurable
    double g=9.81;

    //Parameters of liquid
    int RHO=1000;
    double MU=0.00152;
    double h=0.05;
    double ZETA=0.07;

    //Parameters of liquid with respect to time
    double T_MAX=0.5;
    double DELTA_T=1.0/256;

    //Parameter of the tank
    double L=1.0;
    double N=20;
    double l=(L/N);

    //Parameters for external vibrations
    double A=0.0035;
    double F_REQ=0.386;

    double m = 0; 		// to be initialized
    double cc = 0 ;		//damping coeff
    double kb = 0;		// base spring
    double cb = 0;		// connecting damper
    double OMEGA = 0;

    double 	ETA=0;

    public EquationSloshing()
    {
        this.cc = ZETA / l ;
        this.OMEGA = 2 * Math.PI * F_REQ;
        this.cb = MU * l * Math.sqrt((OMEGA * RHO)/(2*MU));
        this.kb = - OMEGA * cb;
        this.m = RHO * l * h;
    }

    // Calculate dfi
    public double getdfi(double eta,double x0_dot, double x1_dot, double x0,double x1, double x0_ddot,double x1_ddot){
        double alpha, beta, gamma;
        alpha=0.5*  RHO * g *((eta*eta)+(2*eta*h));
        beta=((1/3)*RHO*(eta+h)*(eta+h)*(2*h*l*(x1_dot-x0_dot)*(x1_dot-x0_dot))/((x1-x0+l)*(x1-x0+l)*(x1-x0+l)));
        gamma=((1/3)*RHO*h*l*(eta+h)*(eta+h)*(x1_ddot-x0_ddot)/((x1-x0+l)*(x1-x0+l)*(x1-x0+l)));
        return alpha+beta-gamma;
    }
    
    // Calculate ETA
    public double getEta(double x1, double x0){
        return h*((l/(x1-x0+l))-1);
    }

    // Calculate Fe
    public double getFe(double t){
        return -m* OMEGA * OMEGA * A *(Math.sin(OMEGA*t));
    }

	public double getValue(double t, double[] previousX) {
		double f = 0 ,df1 = 0,df2=0,fe;
        double x0=previousX[0],x0_dot=previousX[1],x0_ddot=previousX[2];
        double x1=previousX[3],x1_dot=previousX[4],x1_ddot=previousX[5];
        double x2=previousX[6],x2_dot=previousX[7],x2_ddot=previousX[8];
        ETA=getEta(x1,x0);
        df1=getdfi(ETA,x0_dot,x1_dot,x0,x1,x0_ddot,x1_ddot);
        df2=getdfi(ETA,x1_dot,x2_dot,x1,x2,x1_ddot,x2_ddot);
        fe=getFe(t);
        f=(df1-df2+(cc*x0_dot)-(((2*cc)+cb)*x1_dot)+(cc*x2_dot)-(kb*x1)+fe);
        double d2y = f / m;
        return d2y;
	}
	
}
