const URL = "http://localhost:8081/";
const submitButton = document.getElementById("request-form-submit");
const errorMessage = document.getElementById("error-message");

submitButton.addEventListener("click", addReimbursement);

$(document).on('keydown', 'input[pattern]', function(e){
    var input = $(this);
    var oldVal = input.val();
    var regex = new RegExp(input.attr('pattern'), 'g');
  
    setTimeout(function(){
      var newVal = input.val();
      if(!regex.test(newVal)){
        input.val(oldVal); 
      }
    }, 1);
  });


async function getNewReimbursement(){
  let newAmount = document.getElementById("reimburse-amount").value;
  if(!newAmount)
  {
    errorMessage.innerText = "Reimbursement amount cannot be left blank.";
    errorMessage.hidden = false;
    return null;
  }

  let reimburseType = document.getElementById("reimburse-type").value;
  let reimDescription = document.getElementById("reimburse-description").value;
  let userID = sessionStorage.getItem("currentLoginID");

  let user = await getUser(userID);

  const now = new Date();
  errorMessage.hidden = true;

  let reimbursement =  {
    author:user,
    amount:newAmount,
    description:reimDescription,
    status:"Pending",
    type:reimburseType,
    resolver:null,
    submitTime:now.toUTCString(),
    resolveTime:null
  }

   return reimbursement;
}

async function getUser(id)
{
    let response = await fetch(URL+"users/"+id);
    if(response.status === 200){
        let data = await response.json();
        return data;
    }else{
        console.log("Cannot get user information.");
        return null;
    }
}

async function addReimbursement(){
  let reimbursement = await getNewReimbursement();
  if(!reimbursement) return;
  let response = await fetch(URL+"reimbursements", {
    method:'POST',
    body:JSON.stringify(reimbursement)
  });

  if(response.status===201){
    console.log("Reimbursement request created successfully.");
    window.location.href = "employeemenu.html?" + sessionStorage.getItem("currentLoginName");
  }else{
    console.log("Something went wrong creating your request.")
  }
}