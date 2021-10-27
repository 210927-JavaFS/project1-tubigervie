const URL = "http://localhost:8081/";

//let addReimbursementButton = document.getElementById('addReimbursementButton');

//addReimbursementButton.onclick = addReimbursement;

async function getReimbursements()
{
  let response = await fetch(URL+"reimbursements");
  if(response.status === 200){
    let data = await response.json();
    populateReimbursementsTable(data, true);
  }else{
    console.log("Cannot get reimbursements.");
  }
}

async function getReimbursementsFromUser()
{
  let user = sessionStorage.getItem("currentLoginName");
  let response = await fetch(URL+"reimbursements/"+user);
  if(response.status === 200){
    let data = await response.json();
    populateReimbursementsTable(data, false);
  }else{
    console.log("Cannot get reimbursements.");
  }
}

function populateReimbursementsTable(data, displayAuthor){
  let tbody = document.getElementById("reimbursementBody");

  tbody.innerHTML="";

  let entryCount = Object.keys(data).length;

  if(entryCount == 0)
  {
    let row = document.createElement("tr");
    let td = document.createElement("td");
    row.appendChild(td);
    row.appendChild(td);
    row.appendChild(td);
    row.appendChild(td);
    row.appendChild(td);
    row.appendChild(td);
    row.appendChild(td);
    row.appendChild(td);
    tbody.appendChild(row);
    return;
  }

  for(let reimbursement of data){
    let row = document.createElement("tr");
    row.setAttribute("class", "clickable-row");
    let tdID = document.createElement("td");
    tdID.setAttribute("class", "col-sm-1");
    tdID.innerText = reimbursement["id"];
    row.appendChild(tdID);

    let tdAmount = document.createElement("td");
    tdAmount.setAttribute("class", "col-sm-1");
    tdAmount.innerText = "$" + reimbursement["amount"];
    row.appendChild(tdAmount);

    let tdDescrip = document.createElement("td");
    tdDescrip.setAttribute("class", "col-sm-1");
    tdDescrip.innerText = reimbursement["description"];
    row.appendChild(tdDescrip);

    let tdType = document.createElement("td");
    tdType.setAttribute("class", "col-sm-1");
    tdType.innerText = reimbursement["type"];
    row.appendChild(tdType);

    let tdStatus = document.createElement("td");
    tdStatus.setAttribute("class", "col-sm-1");
    tdStatus.innerText = reimbursement["status"];
    row.appendChild(tdStatus);

    let tdSubmit = document.createElement("td");
    tdSubmit.setAttribute("class", "col-sm-1");
    tdSubmit.innerText = reimbursement["submitTime"];
    if(!tdSubmit.innerText) tdSubmit.innerText = "N/A";
    row.appendChild(tdSubmit);

    if(displayAuthor)
    {
      let tdAuthor = document.createElement("td");
      tdAuthor.setAttribute("class", "col-sm-1");
      tdAuthor.innerText = reimbursement["author"].username;
      row.appendChild(tdAuthor);
    }

    let tdResolveTime = document.createElement("td");
    tdResolveTime.setAttribute("class", "col-sm-1");
    tdResolveTime.innerText = reimbursement["resolveTime"];
    if(!tdResolveTime.innerText) tdResolveTime.innerText = "N/A";
    row.appendChild(tdResolveTime);

    let tdResolver = document.createElement("td");
    tdResolver.setAttribute("class", "col-sm-1");
    if(!reimbursement["resolver"]) tdResolver.innerText = "N/A";
    else tdResolver.innerText = reimbursement["resolver"].username;
    row.appendChild(tdResolver);
    tbody.appendChild(row);
  }
  if(displayAuthor)
  {
    $('.clickable-row').click(function () {
      let id = $(this).find('td:first').text();
      sessionStorage.setItem("currentReimbursementID", parseInt(id));
      window.location.href = "reimbursement.html?"+id;
    });
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