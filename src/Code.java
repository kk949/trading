

public class Code {

	
		public static void main(String[] args) {
			new Code();
		}
		
		// Run algo
		public Code(){ System.out.println("hi kailash");
			// TODO Currency configuration
			String[] ccyList = {"AUD_CAD"};/* , "AUD_JPY", "AUD_NZD", "AUD_USD", "CAD_JPY", "CHF_JPY", "EUR_AUD", "EUR_CAD", "EUR_CHF",
					"EUR_GBP", "EUR_JPY", "EUR_NZD", "EUR_USD", "GBP_AUD", "GBP_CAD", "GBP_CHF", "GBP_JPY", "GBP_USD", "NZD_CAD",
					"NZD_JPY", "NZD_USD", "USD_CAD", "USD_CHF", "USD_JPY"}; */
			int length = ccyList.length;
			
			FXrateTrading[] trading = new FXrateTrading[length];
			
			// TODO Threshold configuration  (see below)
			double[] deltaS = {0.25/100.0, 0.5/100.0, 1.0/100.0, 1.5/100.0};
			
			for( int i = 0; i < length; ++i ){
				trading[i] = new FXrateTrading(ccyList[i], deltaS.length, deltaS);
			}
			
			// Run
			PriceFeedData p = new PriceFeedData();
			for( int i = 0; i < length; ++i ){
				trading[i].runTradingAsymm(p);
			}	
        }
}