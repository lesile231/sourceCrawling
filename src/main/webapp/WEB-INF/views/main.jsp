<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Source Crawler</title>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js?ver=2" ></script>
	<script type="text/javascript">
	
	$(document).ready(function(){
		
		var prevString = "";
		var prevURL = "";
		var prevOutputType = "";
		
		$("#printResult").on("click", function() {
			
			if( !validateInputValues() )
				return;
			
			// URL과 출력 타입이 변경되지 않았을 경우에는 데이터를 재조회하지 않고, 출력 묶음 단위만 바꿔서 결과를 출력해준다.
			if( prevURL != "" && prevURL == $("#url").val() && prevOutputType != "" && prevOutputType == $("#outputType").val() ) {
				
				printQuotientRemainder( prevString );
				return;
			}
		
			$("#quotient").text( "" );
			$("#remainder").text( "" );
				
			$.ajax({
				url : "crawlingFromUrl.do",
				type : "post",
				data : {
					"url" 		: $("#url").val(),
					"outputType"	: $("#outputType").val()
				},
				success : function( data ) {
					
					if( data.characterString.length == 0 ) {
						$("#quotient").text( "조회된 소스가 없습니다." );
						$("#remainder").text( "조회된 소스가 없습니다." );
						return;
					}
						
					prevURL = $("#url").val();
					prevOutputType = $("#outputType").val();
					prevString = data.characterString;
					
					printQuotientRemainder( prevString );
					
				},
				error : function() {
					alert("서버 접속 실패.");
				}
			});
		});
		
	});
	
	
	function validateInputValues() {
		
		var result = false;
		
		if( $("#url").val() == null ) {
			alert( "URL을 입력해주세요." );
			return result;
		}
		
		if( $("#outputType").val() === null || $("#outputType").val() === "" ) {
			alert( "출력 타입을 입력해주세요." );
			return result;
		}
		
		if( $("#unitLength").val() === null || $("#unitLength").val() === "" ) {
			alert( "출력 묶음 단위를 입력해주세요." );
			return result;
		}
		
		return true;
	}
	
	
	function printQuotientRemainder( characterString ) {
		
		var unitLength = $("#unitLength").val();
		
		var quotient = "";
		var remainder = "";
		
		var characterStringLength = characterString.length;
		var remainderLength = characterStringLength % unitLength;
		var quotientLength = characterStringLength - remainderLength;
		
		for( var i=0; i<quotientLength; i++ ) {
			quotient += characterString[i];
		}
		
		var diff = characterStringLength-remainderLength;
		
		for( var i=diff; i<characterStringLength; i++) {
			remainder += characterString[i];
		}
		
		$("#quotient").text( quotient );
		$("#remainder").text( remainder );
	}
	
	
	</script>
	
	<style>
	div {
		margin:10px;
	}
	
	button {
		margin-left:218px;
		width: 100px;
		height: 50px;
	}
	
	input {
		width:180px;
		height:18px;
	}
	
	select {
		width:180px;
		height:25px;
	}
	</style>
	
</head>
<body>
<h1>
	Source Crawler
</h1>

<div>
	<div>
		URL : <input type="text" id="url"></input>
	</div>
	
	<div>
		출력 타입 : 
		<select id="outputType">
			<option value="">선택</option>
			<option value="TEXT">TEXT</option>
			<option value="HTML">HTML</option>
		</select>
	</div>
	
	<div>
		출력 묶음 단위 : <input type="text" id="unitLength"/>
	</div>
	
	<div>
		<button id="printResult"> 결과 출력 </button>
	</div>
	
	<div class="result">
		<div class="quotient">
			몫 : <span id="quotient"></span>
		</div>
			
		<div class="remainder">
			나머지 : <span id="remainder"></span>
		</div>
	</div>
</div>
</body>
</html>
