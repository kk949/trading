import java.io.FileWriter;
import java.io.IOException;



public class FXrateTrading{
		CoastlineTrader[] coastTraderLong, coastTraderShort;
		String FXrate;
		Liquidity liquidity;
		double currentTime, oneDay;
		
		FXrateTrading(){};
		FXrateTrading(String rate, int nbOfCoastTraders, double[] deltas){
			currentTime = 1136073600000.0;
			oneDay = 24.0*60.0*60.0*1000.0;
			FXrate = new String(rate);
			coastTraderLong = new CoastlineTrader[nbOfCoastTraders];
			coastTraderShort = new CoastlineTrader[nbOfCoastTraders];
			
			for( int i = 0; i < coastTraderLong.length; ++i ){
				coastTraderLong[i] = new CoastlineTrader(deltas[i], deltas[i], deltas[i], deltas[i], rate.toString(), 1);
				coastTraderShort[i] =  new CoastlineTrader(deltas[i], deltas[i], deltas[i], deltas[i], rate.toString(), -1);
			}
		};
		
		boolean runTradingAsymm(PriceFeedData price){
			for( int i = 0; i < coastTraderLong.length; ++i ){
				coastTraderLong[i].runPriceAsymm(price, coastTraderShort[i].tP);
				coastTraderShort[i].runPriceAsymm(price, coastTraderLong[i].tP);
			}
			
			if( price.elems.time >= currentTime + oneDay ){
				while( currentTime <= price.elems.time )
					currentTime += oneDay;
				
				printDataAsymm(currentTime);
			}
			return true;
		}
		
		boolean printDataAsymm(double time){
			String sep = new String(System.getProperty("file.separator"));
//			String folder = new String(sep + "Users" + sep + "deepakupadhyay" + sep + "Downloads" + sep + "Code-master" + sep + FXrate.toString() + "DataAsymmLiq.dat");
			String folder = new String(sep + "home" + sep + "pods" + sep + "Downloads" +  sep + FXrate.toString() + "DataAsymmLiq.dat");
			FileWriter fw = null;
			
			try{
				double totalPos = 0.0, totalShort = 0.0, totalLong = 0.0; double totalPnl = 0.0; double totalPnlPerc = 0.0;  
				fw = new FileWriter(folder, true);
				double price = -1.0;
				for( int i = 0; i < coastTraderLong.length; ++i ){
					if( i == 0 ){
						price = coastTraderLong[i].lastPrice;
					}
					totalLong += coastTraderLong[i].tP;
					totalShort += coastTraderShort[i].tP;
					totalPos += (coastTraderLong[i].tP + coastTraderShort[i].tP);
					totalPnl += (coastTraderLong[i].pnl + coastTraderLong[i].tempPnl + coastTraderLong[i].computePnlLastPrice()
							+ coastTraderShort[i].pnl + coastTraderShort[i].tempPnl + coastTraderShort[i].computePnlLastPrice());
					totalPnlPerc += (coastTraderLong[i].pnlPerc + (coastTraderLong[i].tempPnl + coastTraderLong[i].computePnlLastPrice())/coastTraderLong[i].cashLimit*coastTraderLong[i].profitTarget
							+ coastTraderShort[i].pnlPerc + (coastTraderShort[i].tempPnl + coastTraderShort[i].computePnlLastPrice())/coastTraderShort[i].cashLimit*coastTraderShort[i].profitTarget);
				}
				fw.append((long)time + "," + totalPnl + "," + totalPnlPerc + "," + totalPos + "," + totalLong + "," + totalShort + "," + price + "\n");
				fw.close();
			}
			catch(IOException e){
				System.out.println("Failed opening DC thresh file! " + e.getMessage());
				return false;
			}
			return true;
		};
	};