<html>
	<head>
	<script src="//code.jquery.com/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="typeahead.js"></script>
	<link href="typeahead.css"  rel="stylesheet" />
	<script type="text/javascript" rel="stylesheet">
	$(document).ready(function(){
		var countries = ["Afghanistan", "Albania", "Bahamas", "Bahrain", "Cambodia", "Cameroon", "Denmark", "Djibouti", "East Timor", "Ecuador", "Falkland Islands (Malvinas)", "Faroe Islands", "Gabon", "Gambia", "Haiti", "Heard and Mc Donald Islands", "Iceland", "India", "Jamaica", "Japan", "Kenya", "Kiribati", "Lao People's Democratic Republic", "Latvia", "Macau", "Macedonia", "Namibia", "Nauru", "Oman", "Pakistan", "Palau", "Qatar", "Reunion", "Romania", "Saint Kitts and Nevis", "Saint Lucia", "Taiwan", "Tajikistan", "Uganda", "Ukraine", "Vanuatu", "Vatican City State", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zaire", "Zambia"];
		
		var countries_suggestion = new Bloodhound({
			datumTokenizer: Bloodhound.tokenizers.whitespace,
			queryTokenizer: Bloodhound.tokenizers.whitespace,
			local: countries
		});
		
		$('.typeahead').typeahead(
			{ minLength: 1 },
			{ source: countries_suggestion }
		);
	});  
	</script>
	<style>
	.typeahead { border: 1px solid #CCCCCC;border-radius: 4px;padding: 8px 12px;width: 300px;font-size:1.5em;}
	.tt-menu { width:300px; }
	span.twitter-typeahead .tt-suggestion {padding: 10px 20px;	border-bottom:#CCC 1px solid;cursor:pointer;}
	span.twitter-typeahead .tt-suggestion:last-child { border-bottom:0px; }
	.bgColor {max-width: 440px;height: 200px;background-color: #c3e8cb;padding: 40px 70px;border-radius:4px;margin:20px auto;}
	.demo-label {font-size:1.5em;color: #686868;font-weight: 500;}
	</style>
	</head>
	<body>
	<div class="bgcolor">
	<label class="demo-label">Search:</label> <input type="text" name="country" class="typeahead" />
	</div>
	</body>
</html>