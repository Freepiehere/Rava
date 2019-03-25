SMS is a simple personal assistance app which contains few features. Using smtp to access a personal gmail account and listing a personal phone number with service provider as the email recipient, (in Sender.java) SMS can contact the user by sending texts to them directly.
The first feature (com.umbrellaProtocol) connects to a weather service api (in DarkSkies.java) which responds with current, hourly, or daily weather data in json format. The weather data is composed into a (visual/numerical) report and sent to the user. Given the predicted precipitation and temperature values recommendations are made to bring an umbrella or what layers of clothing are appropriate.

Classes:
	
	DarkSkies.java:
	DarkSkies.java connects to the developer friendly DarkSkies weather api. DarkSkies allows up to 1'000 free calls a day. Api calls require a secret api key (freely acquired) and geographic coordinates (longitude, latitude). Sample api call: "https://api.darksky.net/forecast/393c3c68c626da225db4e6fd2af2b560/37.8267,-122.4233".
		Fields:
			String sumamry
			String[] metrics
			double[][] weather
			String[] time
		Methods:
			DarkSkies():
			connectDarkSkies(String, String, String):
			parseHourly(HttpConnection):
			weather_summary(jsonObjecy):
			weather_data(jsonObject):
			weather_fields(jsonArray, String[]):
			getSummary():
			getMetrics():
			getTime();

	Sender.java:
		Fields:
			Message message
		Methods:
			Sender(String, String):
			declareProperties():
			declareSession(Properties, String, String):
			sendMessage():
			setMessage(String, String):
			getMessage():
			
