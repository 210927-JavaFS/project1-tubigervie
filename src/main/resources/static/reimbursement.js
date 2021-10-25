const URL = "http://localhost:8081/";

let addReimbursementButton = document.getElementById('addReimbursementButton');
let viewReimbursementsButton = document.getElementById('viewReimbursementsButton');

addReimbursementButton.onclick = addReimbursement;
viewReimbursementsButton.onclick = getReimbursementsFromUser;

async function getReimbursements()
{
  let response = await fetch(URL+"reimbursements");
  if(response.status === 200){
    let data = await response.json();
    populateReimbursementsTable(data);
  }else{
    console.log("Cannot get reimbursements.");
  }
}

async function getReimbursementsFromUser()
{
  let user = sessionStorage.getItem("currentlogin");
  console.log("fetching reimbursements for: " + user);
  let response = await fetch(URL+"reimbursements/"+user);
  if(response.status === 200){
    let data = await response.json();
    populateReimbursementsTable(data);
  }else{
    console.log("Cannot get reimbursements.");
  }
}

function populateReimbursementsTable(data){
  let tbody = document.getElementById("reimbursementBody");

  tbody.innerHTML="";

  for(let reimbursement of data){
    let row = document.createElement("tr");

    for(let cell in reimbursement){
      let td = document.createElement("td");
      td.setAttribute("class", "col-sm-1");
      if(cell != "id")
      {
        td.innerText=reimbursement[cell];
        row.appendChild(td);
      }
    }
    tbody.appendChild(row);
  }
}


function getNewReimbursement(){
  let newAmount = document.getElementById("reimbursementAmount").value;

  let reimbursement =  {
    amount:newAmount,
    description:"this is a test description",
    status:"Pending",
    type:"Other"
  }

  return reimbursement;
}

async function addReimbursement(){
  let reimbursement = getNewReimbursement();

  let response = await fetch(URL+"reimbursements", {
    method:'POST',
    body:JSON.stringify(reimbursement)
  });

  if(response.status===201){
    console.log("Reimbursement request created successfully.");
  }else{
    console.log("Something went wrong creating your request.")
  }

}