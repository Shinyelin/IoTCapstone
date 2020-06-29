<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_gametest.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전>달 받습니다.
        $g3School=$_POST['g3School'];
        $g3Grade=$_POST['g3Grade'];
        $g3Class=$_POST['g3Class'];

        $g3Score=$_POST['g3Score'];
        if(empty($g3School)){
            $errMSG = "학교를 입력하세요.";
        }
        else if(empty($g3Grade)){
            $errMSG = "학년을 입력하세요.";
        }
                else if(empty($g3Class)){
            $errMSG = "반을 입력하세요.";
        }

        else if(empty($g3Score)){
            $errMSG = "점수를 입력하세요.";
        }


        if(!isset($errMSG)) // 모두 입력이 되었다면
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 car 테이블에 저장합니>다.
                $stmt = $con->prepare('INSERT INTO game3(g3School, g3Grade, g3Class, g3Score) VALUES(:g3School, :g3Grade, :g3Class, :g3Score)');
                $stmt->bindParam(':g3School', $g3School);
                $stmt->bindParam(':g3Grade', $g3Grade);
                 $stmt->bindParam(':g3Class', $g3Class);
           $stmt->bindParam(':g3Score', $g3Score);
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
                학교: <input type = "text" name = "g3School" />
                학년: <input type = "text" name = "g3Grade" />
                반: <input type = "text" name = "g3Class" />
                    점수: <input type = "text" name = "g3Score" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
?>
