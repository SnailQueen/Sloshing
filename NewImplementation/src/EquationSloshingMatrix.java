
public class EquationSloshingMatrix {
    //this parameter is not configurable
    double g=9.81;

    //Parameters of liquid
    int RHO=1000;
    double MU=0.00152;
    double h=0.05;
    double ZETA=0.07;

    //Parameters of liquid with respect to time
    int T_MAX=8;
    double DELTA_T=1.0/256;

    //Parameter of the tank
    double L=1.0;
    int N=100;
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

    public EquationSloshingMatrix()
    {
        this.cc = ZETA / l ;
        this.OMEGA = 2 * Math.PI * F_REQ;
        this.cb = MU * l * Math.sqrt((OMEGA * RHO)/(2*MU));
        this.kb = - OMEGA * cb;
        this.m = RHO * l * h;
    }

    // Calculate alpha + beta
    public double getAlphaPlusBeta (double eta,double x0_dot, double x1_dot, double x0,double x1) {
    	double alpha, beta;
        alpha=0.5*  RHO * g *((eta*eta)+(2*eta*h));
        beta=((1/3) *RHO*(eta+h)*(eta+h)*(2*h*l*(x1_dot-x0_dot)*(x1_dot-x0_dot))/((x1-x0+l)*(x1-x0+l)*(x1-x0+l)));
        return alpha + beta;
    }
    
    // Calculate ETA
    public double getEta(double x1, double x0){
        return h*((l/(x1-x0+l))-1);
    }

    // Calculate Fe
    public double getFe(double t){
        return -m* (OMEGA*OMEGA)* A *(Math.sin(OMEGA*t));
    }
    

    //get value of F(x,xdot)
    //               0      1         2              3        4          5            6       7          8
    //preResult[]= {x0,   x0_dot,    x0_doubleDot,  x1,    x1_dot,   x1_doubleDot,   x2,   x2_dot,   x2_doubleDot}

	
	public double getValue(double t, double[] previousX) {
		double f = 0 ,alphaPlusBeta1 = 0, alphaPlusBeta2=0,fe;
        double x0=previousX[0],x0_dot=previousX[1],x0_ddot=previousX[2];
        double x1=previousX[3],x1_dot=previousX[4],x1_ddot=previousX[5];
        double x2=previousX[6],x2_dot=previousX[7],x2_ddot=previousX[8];
        ETA=getEta(x1,x0);
        alphaPlusBeta1=getAlphaPlusBeta(ETA,x0_dot,x1_dot,x0,x1);
        alphaPlusBeta2=getAlphaPlusBeta(ETA,x1_dot,x2_dot,x1,x2);
        fe=getFe(t);
        f=(alphaPlusBeta1 - alphaPlusBeta2) - (((2 * cc) + cb) * x1_dot) + (cc * x2_dot) - (kb * x1) + fe;
        return f;
	}
	
	// Calculate Gamma i
	public double getGammaCurrent (double[] previousX) {
		double x0=previousX[0],x0_dot=previousX[1],x0_ddot=previousX[2];
        double x1=previousX[3],x1_dot=previousX[4],x1_ddot=previousX[5];
        double x2=previousX[6],x2_dot=previousX[7],x2_ddot=previousX[8];
        ETA=getEta(x1,x0);
        return (1/3)*RHO*h*l*((ETA+h)*(ETA+h))*(x1_ddot-x0_ddot)/((x1-x0+l)*(x1-x0+l));
	}
	
	// Calculate Gamma i+1
		public double getGammaNext (double[] previousX) {
			double x0=previousX[0],x0_dot=previousX[1],x0_ddot=previousX[2];
	        double x1=previousX[3],x1_dot=previousX[4],x1_ddot=previousX[5];
	        double x2=previousX[6],x2_dot=previousX[7],x2_ddot=previousX[8];
	        ETA=getEta(x2,x1);
	        return (1/3)*RHO*h*l*((ETA+h)*(ETA+h))*(x2_ddot-x1_ddot)/((x2-x1+l)*(x2-x1+l));
		}
	
	
	/* In the following way, ETA has been substituted.
    // Calculate Gamma i
    public double getGammaCurrent (double[] previousX) {
    	double x0=previousX[0],x0_dot=previousX[1],x0_ddot=previousX[2];
        double x1=previousX[3],x1_dot=previousX[4],x1_ddot=previousX[5];
        double x2=previousX[6],x2_dot=previousX[7],x2_ddot=previousX[8];
    	return ( RHO * (Math.pow(h, 3)) * (Math.pow(l, 3)) ) / ( 3 * (Math.pow((x1 - x0 + l), 4)));
    }
    
 // Calculate Gamma i+1
    public double getGammaNext (double[] previousX) {
    	double x0=previousX[0],x0_dot=previousX[1],x0_ddot=previousX[2];
        double x1=previousX[3],x1_dot=previousX[4],x1_ddot=previousX[5];
        double x2=previousX[6],x2_dot=previousX[7],x2_ddot=previousX[8];
    	return ( RHO * (Math.pow(h, 3)) * (Math.pow(l, 3)) ) / ( 3 * (Math.pow((x2 - x1 + l), 4)));
    }
	*/
}
