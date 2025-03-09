$(document).ready(function () {
    $("#login").click(function () {
        let email = $("#exampleInputEmail1").val();
        let password = $("#exampleInputPassword").val();

        // 입력값 검증
        if (!email || !password) {
            alert("이메일과 비밀번호를 입력하세요.");
            return;
        }
    });
});