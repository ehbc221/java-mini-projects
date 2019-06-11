document.getElementById('save').addEventListener('submit',function() {
    $('#response').html("<center><img src=\"/system/ressource?type=image&ref=progress.gif\" alt=\"wait for a moment ...\" /></center>");
}, true);
document.getElementById('saveRepertoryFrame').submit();
function displaywait(repertory){
    $('#response').html("");
    $('#repertoryFrame').html(repertory);
}
$('#images').change(function(){
    $('#uploadFrame').html("<iframe name=\"uploadFrame\"></iframe>");
});