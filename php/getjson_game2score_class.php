<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon_schoolinfo.php');



//POST 값을 읽어온다.
$g2School=isset($_POST['g2School']) ? $_POST['g2School'] : '';
$g2Grade = isset($_POST['g2Grade']) ? $_POST['g2Grade'] : '';


$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($g2School != "" ){

    $sql="select id,g2Grade,g2School,g2Class, sum(g2score) as sumg2 from game2 where g2School='$g2School' and g2Grade='$g2Grade' group by g2Class order by sum(g2score) desc";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        echo "'";
        echo $g2School,", ",$g2Grade;
        echo "'은 찾을 수 없습니다.";
    }
    if($stmt ->rowCount()>0)
    {

        $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

                extract($row);


            array_push($data,
                array('id'=>$id,
                'g2School'=>$g2School,
                'g2Grade'=>$g2Grade,
                'g2Class'=>$g2Class,
                'sumg2'=>$sumg2

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
         학교이름: <input type = "text" name = "g2School" />
         학년: <input type = "text" name = "g2Grade" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}


?>
