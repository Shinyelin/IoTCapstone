
<?php
error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_schoolinfo.php');


    $stmt = $con->prepare('select g2school, sum(g2Score) as sumg2 from game2 group by g2School order by sum(g2score) desc');
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);

            array_push($data,
                array(
                'g2school'=>$g2school,
                'sumg2' =>$sumg2
           ));
        }
function han ($s) { return reset(json_decode('{"s":"'.$s.'"}')); }
function to_han ($str) { return preg_replace('/(\\\u[a-f0-9]+)+/e','han("$0")',$str); }

        header('Content-Type: application/json; charset=utf8');
        $json = to_han(json_encode(array("webnautes"=>$data)));
        echo $json;
    }

?>
