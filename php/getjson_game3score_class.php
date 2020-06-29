<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon_schoolinfo.php');



//POST 값을 읽어온다.
$g3School=isset($_POST['g3School']) ? $_POST['g3School'] : '';
$g3Grade = isset($_POST['g3Grade']) ? $_POST['g3Grade'] : '';


$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($g3School != "" ){

    $sql="select id,g3Grade,g3School,g3Class, sum(g3score) as sumg3 from game3 where g3School='$g3School' and g3Grade='$g3Grade' group by g3Class order by sum(g3score) desc";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        echo "'";
        echo $g3School,", ",$g3Grade;
        echo "'은 찾을 수 없습니다.";
    }
    if($stmt ->rowCount()>0)
    {

        $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

                extract($row);

            array_push($data,
                array('id'=>$id,
                'g3School'=>$g3School,
                'g3Grade'=>$g3Grade,
                'g3Class'=>$g3Class,
                'sumg3'=>$sumg3

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
         학교이름: <input type = "text" name = "g3School" />
         학년: <input type = "text" name = "g3Grade" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}


?>
