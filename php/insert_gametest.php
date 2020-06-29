<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_gametest.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전>달 받습니다.
        $g1School=$_POST['g1School'];
        $g1Grade=$_POST['g1Grade'];
        $g1Class=$_POST['g1Class'];
        $walkCount=$_POST['walkCount'];
        $g1Score=$_POST['g1Score'];
        if(empty($g1School)){
            $errMSG = "학교를 입력하세요.";
        }
        else if(empty($g1Grade)){
            $errMSG = "학년을 입력하세요.";
        }
                else if(empty($g1Class)){
            $errMSG = "반을 입력하세요.";
        }
        else if(empty($walkCount)){
            $errMSG = "걸음수를 입력하세요.";
        }
        else if(empty($g1Score)){
            $errMSG = "점수를 입력하세요.";
        }


        if(!isset($errMSG)) // 모두 입력이 되었다면
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 car 테이블에 저장합니>다.
                $stmt = $con->prepare('INSERT INTO game1(g1School, g1Grade, g1Class,  walkCount, g1Score) VALUES(:g1School, :g1Grade, :g1Class,:walkCount, :g1Score)');
                $stmt->bindParam(':g1School', $g1School);
                $stmt->bindParam(':g1Grade', $g1Grade);
                 $stmt->bindParam(':g1Class', $g1Class);

 $stmt->bindParam(':walkCount', $walkCount);
           $stmt->bindParam(':g1Score', $g1Score);
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
                학교: <input type = "text" name = "g1School" />
                학년: <input type = "text" name = "g1Grade" />
                반: <input type = "text" name = "g1Class" />
                걸음수: <input type = "text" name = "walkCount" />
                    점수: <input type = "text" name = "g1Score" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
?>

