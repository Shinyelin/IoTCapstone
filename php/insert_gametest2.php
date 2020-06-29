<?php
error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_gametest.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전>달 받습니다.
        $g2School=$_POST['g2School'];
        $g2Grade=$_POST['g2Grade'];
        $g2Class=$_POST['g2Class'];
        $g2Score=$_POST['g2Score'];
        if(empty($g2School)){
            $errMSG = "학교를 입력하세요.";
        }
        else if(empty($g2Grade)){
            $errMSG = "학년을 입력하세요.";
        }
                else if(empty($g2Class)){
            $errMSG = "반을 입력하세요.";
        }
        else if(empty($g2Score)){
            $errMSG = "점수를 입력하세요.";
        }


        if(!isset($errMSG)) // 모두 입력이 되었다면
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 car 테이블에 저장합니>다.
                $stmt = $con->prepare('INSERT INTO game2(g2School, g2Grade, g2Class, g2Score) VALUES(:g2School, :g2Grade, :g2Class,:g2Score)');
                $stmt->bindParam(':g2School', $g2School);
                $stmt->bindParam(':g2Grade', $g2Grade);
                $stmt->bindParam(':g2Class', $g2Class);
                                $stmt->bindParam(':g2Score', $g2Score);
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
                학교: <input type = "text" name = "g2School" />
                학년: <input type = "text" name = "g2Grade" />
                반: <input type = "text" name = "g2Class" />
                    점수: <input type = "text" name = "g2Score" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
