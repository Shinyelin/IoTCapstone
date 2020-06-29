<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_schoolinfo.php');


    $stmt = $con->prepare('select * from game1 order by g1Score desc');
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);

            array_push($data,
                array( 'g1Grade'=>$g1Grade,
                    'g1Class'=>$g1Class,
                    'walkCount'=>$walkCount,
                'g1Score'=>$g1Score,
                   'g1School'=>$g1School
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("webnautes"=>$data));
        echo $json;
    }
