<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_schoolinfo.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전>달 받습니다.

        $aSchool=$_POST['aSchool'];
        $aGrade=$_POST['aGrade'];
        $aClass=$_POST['aClass'];
                $aPwd=$_POST['aPwd'];

        if(empty($aSchool)){
            $errMSG = "학교이름을 입력하세요.";
        }
        else if(empty($aGrade)){
            $errMSG = "학년을 입력하세요.";
        }
       else if(empty($aClass)){
            $errMSG = "반을 입력하세요.";
        }
                else if(empty($aPwd)){
            $errMSG = "비밀번호을 입력하세요.";
        }

        if(!isset($errMSG)) // 모두 입력이 되었다면
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 admin 테이블에 저장합>니다.
                $stmt = $con->prepare('INSERT INTO schoolinfo(aSchool, aGrade, aClass, aPwd) VALUES(:aSchool, :aGrade, :aClass, :aPwd)');
                $stmt->bindParam(':aSchool', $aSchool);
                                $stmt->bindParam(':aGrade', $aGrade);
                $stmt->bindParam(':aClass', $aClass);
                                $stmt->bindParam(':aPwd', $aPwd);

                if($stmt->execute())
                {
                    $successMSG = "새로운 사용자를 추가했습니다.";
                }
                else
                {
                    $errMSG = "사용자 추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }

    }

?>


<?php
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

        $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                학교이름: <input type = "text" name = "aSchool" />
                                학년: <input type = "text" name = "aGrade" />
                                반: <input type = "text" name = "aClass" />
                                비밀번호: <input type = "text" name = "aPwd" />


                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
?>

