<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="com.kh.spring.demo.model.vo.Dev"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Demo" name="title"/>
</jsp:include>
<style>
div#demo-container{
	width:550px;
	padding:15px;
	border:1px solid lightgray;
	border-radius: 10px;
}
</style>
<div id="demo-container" class="mx-auto">
	<!-- https://getbootstrap.com/docs/4.1/components/forms/#readonly-plain-text -->
	<form id="devFrm"
		  method="post" 
		  action="${pageContext.request.contextPath}/demo/updateDev.do">
		<div class="form-group row">
		  <label for="name" class="col-sm-2 col-form-label">이름</label>
		  <div class="col-sm-10">
		    <input type="text" class="form-control" id="name" name="name" value="${dev.name}" required>
		  </div>
		</div>
		<div class="form-group row">
		  <label for="career" class="col-sm-2 col-form-label">개발경력</label>
		  <div class="col-sm-10">
		    <input type="number" class="form-control" id="career" name="career" value="${dev.career}" required>
		  </div>
		</div>
		<div class="form-group row">
		  <label for="email" class="col-sm-2 col-form-label">이메일</label>
		  <div class="col-sm-10">
		    <input type="email" class="form-control" id="email" name="email" value="${dev.email}" required>
		  </div>
		</div>
	  	<!-- https://getbootstrap.com/docs/4.1/components/forms/#inline -->
	    <div class="form-group row">
	    	<label class="col-sm-2 col-form-label">성별</label>
	    	<div class="col-sm-10">
			    <div class="form-check form-check-inline">
				  <input class="form-check-input" type="radio" name="gender"
				  		 id="gender0" value="M" ${dev.gender eq 'M' ? 'checked' : ''}>
				  <label class="form-check-label" for="gender0">남</label>
				</div>
				<div class="form-check form-check-inline">
				<input class="form-check-input" type="radio" name="gender" 				  		 
						 id="gender1" value="F" ${dev.gender eq 'F' ? 'checked' : ''}>
				<label class="form-check-label" for="gender1">여</label>
				</div>
			</div>
		</div>
		<div class="form-group row">
			<label class="col-sm-2 col-form-label">개발언어</label>
			<div class="col-sm-10">
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Java" value="Java" ${fn:contains(list,'Java') ? 'checked' : ''}>
				  <label class="form-check-label" for="Java">Java</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="C" value="C" ${fn:contains(list,'C') ? 'checked' : ''}>
				  <label class="form-check-label" for="C">C</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Javascript" value="Javascript" ${fn:contains(list,'Javascript') ? 'checked' : ''}>
				  <label class="form-check-label" for="Javascript">Javascript</label>
				</div>
				<div class="form-check form-check-inline">
				  <input class="form-check-input" type="checkbox" name="lang" id="Python" value="Python" ${fn:contains(list,'Python') ? 'checked' : ''}>
				  <label class="form-check-label" for="Python">Python</label>
				</div>
			</div>
		</div>
		<!-- 중요 - 수정시 반드시 고유번호도 함께 넘겨주어야 함 -->
	  	<input type="hidden" name="no" value="${dev.no}" />
	  	<button type="submit" class="list-group-item list-group-item-action">dev 수정</button>
	</form>
</div>

<script>


// 화살표 함수 => : 익명 함수에 대한 표현식
// function(){} : ()=>{}

//1. function 키워드 생략. 매개변수부 => 몸통부
/**
 * var foo = (a, b) => {
	 console.log(a,b);
   };
   foo(10, 3);
 */
 
 //2. 매개변수가 한 개인 경우, 괄호생략 가능
/**
 * var bar = x =>{
	 return x * x;
 };
 bar(3);
 */
 
 //3. 몸통부 코드가 한줄(리턴포함)인 경우, {} 생략 가능
/**
 * bar = x => x * x;
   (return도 생략된다!!)
 */
 
 //4. 화살표함수 안의 this는 보통 window이다.
 // this가 없고, parent scope this를 가져다 쓴다.
 // var too = () => console.log(this);
 // too();   ----> window가 나옴
 // event handler로 사용시에도 this는 event.target이 아니다.
 
 // method로 사용하면 안된다. javascript의 method는 객체 안에 존재하는 것을 말함
/**
 *
	var user = {
		 name : "홍길동",
		 say : function(){
			 console.log(this.name + "이 불라불라~");
		 }
	};
 	
 	아래와 같이 변경됨
 	var user = {
		 name : "홍길동",
		 say : () => {
			 console.log(this.name + "이 불라불라~");
		 }
	};
	아무것도 출력되지 않는다. 이유는, 
	this가 현재 객체를 가리키지 않고, window를 가리키기 때문에 그렇다.
	
	method를 축약하기 위해서는 다른 방식을 써야한다.
	var user = {
		name : "홍길동",
		say() {
			console.log(this.name + "이 불라불라~");
		}
	};
	
	생성자 함수를 사용할 수 없음.
	this 키워드가 window를 가리키기 때문에!
 */

 /**
  * 수정폼 유효성검사
  * 값이 유효하지 않은 경우
  1. return false
  2. event.preventDefault()
  */
  //name 값일떄만 아래처럼 사용 가능
// $(document.devFrm).submit((e) => {
 $(devFrm).submit((e) => {
	//1. 이름은 한글 2글자 이상이어야 한다.
	var $name = $("#name");
     if(/^[가-힣]{2,}$/.test($name.val())==false){
         alert("이름은 한글 2글자 이상이어야 합니다.");
         $name.select();
		 e.preventDefault();
     }
	
	//2. 개발언어는 하나이상 선택해야 한다.
	 var $langChecked = $("[name=lang]:checked");
	 if ($langChecked.length==0){
    	 alert("개발언어는 하나 이상 선택해야 합니다.");
		 e.preventDefault();
     }			
});
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
