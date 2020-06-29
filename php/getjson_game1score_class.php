<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon_schoolinfo.php');

//POST 값을 읽어온다.
$g1School=isset($_POST['g1School']) ? $_POST['g1School'] : '';
$g1Grade = isset($_POST['g1Grade']) ? $_POST['g1Grade'] : '';


$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($g1School != "" ){

    $sql="select id,g1Grade,g1School,g1Class, sum(g1score) as sumg1 from game1 where g1School='$g1School' and g1Grade='$g1Grade' group by g1Class order by sum(g1score) desc";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        echo "'";
        echo $g1School,", ",$g1Grade;
        echo "'은 찾을 수 없습니다.";
    }
    if($stmt ->rowCount()>0)
    {

        $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

                extract($row);

            array_push($data,
                array('id'=>$id,
                'g1School'=>$g1School,
                'g1Grade'=>$g1Grade,
                'g1Class'=>$g1Class,
                'sumg1'=>$sumg1

            ));
        }

      function han ($s) { return reset(json_decode('{"s":"'.$s.'"}')); }
      function to_han ($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

        header('Content-Type: application/json; charset=utf8');
        $json = to_han(json_encode(array("webnautes"=>$data)));
        echo $json;

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
         학교이름: <input type = "text" name = "g1School" />
         학년: <input type = "text" name = "g1Grade" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}


?>

