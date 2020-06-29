<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon_schoolinfo.php');



//POST 값을 읽어온다.
$aSchool=isset($_POST['aSchool']) ? $_POST['aSchool'] : '';
$aGrade = isset($_POST['aGrade']) ? $_POST['aGrade'] : '';
$aClass = isset($_POST['aClass']) ? $_POST['aClass'] : '';
$aPwd = isset($_POST['aPwd']) ? $_POST['aPwd'] : '';

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($aSchool != "" ){

    $sql="select * from schoolinfo where aSchool='$aSchool' and aGrade='$aGrade'and aClass='$aClass'and aPwd='$aPwd'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        echo "'";
        echo $aSchool,", ",$aGrade;
        echo "'은 찾을 수 없습니다.";
    }
        else{

                $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

                extract($row);

            array_push($data,
                array('id'=>$row["id"],
                'aSchool'=>$row["aSchool"],
                'aGrade'=>$row["aGrade"],
                                'aClass'=>$row["aClass"],
                                'aPwd'=>$row["aPwd"]
            ));
        }


        if (!$android) {
            echo "<pre>";
            print_r($data);
            echo '</pre>';
        }else
        {
            echo "환영합니다";
        }
    }
}
else {
    echo "아이디를 입력하세요";
}

?>



<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
         학교이름: <input type = "text" name = "aSchool" />
         학년: <input type = "text" name = "aGrade" />
                 반: <input type = "text" name = "aClass" />
                 비밀번호: <input type = "text" name = "aPwd" />


         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}


?>
