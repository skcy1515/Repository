$(document).ready(function () {
    let isEmailVerified = false;  // 이메일 인증 여부
    let userEmail = "";  // 사용자의 이메일 저장

    // 이메일 인증번호 요청
    $("#verify1").click(function () {
        userEmail = $("#exampleInputEmail1").val();
        if (!userEmail) {
            alert("이메일을 입력하세요.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/send-verification",
            data: { email: userEmail },
            success: function (response) {
                alert("인증번호가 이메일로 전송되었습니다.");
                $("#checkEmail").show(); // 인증번호 입력칸 보이기
            },
            error: function () {
                alert("이메일 전송 실패! 올바른 이메일인지 확인하세요.");
            }
        });
    });

    // 인증번호 확인
    $("#verify2").click(function () {
        let code = $("#text").val();
        if (!code) {
            alert("인증번호를 입력하세요.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/verify-code",
            data: { email: userEmail, code: code },
            success: function (response) {
                alert("이메일 인증이 완료되었습니다!");
                isEmailVerified = true;
                // 이메일 입력칸, 인증번호칸, 인증번호 요청, 인증 버튼 비활성화
                $("#exampleInputEmail1").prop("disabled", true);
                $("#text").prop("disabled", true);
                $(".btn-primary:contains('인증')").prop("disabled", true);
            },
            error: function () {
                alert("인증번호가 올바르지 않습니다.");
            }
        });
    });

    // 회원가입 처리
    $(".btn-primary:contains('회원가입')").click(function () {
        if (!isEmailVerified) {
            alert("이메일 인증을 먼저 완료하세요.");
            return;
        }

        let nickname = $("#text2").val();
        let password = $("#exampleInputPassword1").val();
        let confirmPassword = $("#exampleInputPassword2").val();

        if (!password || !confirmPassword) {
            alert("비밀번호를 입력하세요.");
            return;
        }
        if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/registerSMTP",
            contentType: "application/json",
            data: JSON.stringify({ email: userEmail, password: password, nickname: nickname }),
            success: function (response) {
                alert("회원가입이 완료되었습니다!");
                window.location.href = "/signIn";
            },
            error: function (xhr) {
                if (xhr.status === 404) {
                    alert(xhr.responseText);
                } else {
                    alert("회원가입 실패! 다시 시도하세요.");
                }
            }
        });
    });
});
