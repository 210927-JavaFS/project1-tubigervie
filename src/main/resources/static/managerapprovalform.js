const URL = "http://54.193.215.129:8081/";
const acceptButton = document.getElementById("accept");
const declineButton = document.getElementById("decline");

acceptButton.onclick = approveReimbursement;
declineButton.onclick = denyReimbursement;

async function getReimbursement(id)
{
  let user = sessionStorage.getItem("currentLoginName");
  let response = await fetch(URL+"reimbursements/"+user+"/"+id);
  if(response.status === 200){
    let data = await response.json();
    fillInReimbursementInfo(data);
  }else{
    console.log("Cannot get reimbursement.");
  }
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

async function approveReimbursement()
{
    let reimburseID = sessionStorage.getItem("currentReimbursementID");
    let userID = sessionStorage.getItem("currentLoginID");
    let user = sessionStorage.getItem("currentLoginName");
    let adminUser = await getUser(userID);
    
    let response = await fetch(URL+"reimbursements/"+user+"/"+reimburseID);
    if(response.status === 200){
        let data = await response.json();
        const now = new Date();
        let updatedReimbursement = {
            id : data["id"],
            amount : data["amount"],
            description : data["description"],
            author : data["author"],
            status : "Approved",
            type : data["type"],
            resolver : adminUser,
            submitTime : data["submitTime"],
            resolveTime : now.toUTCString()
        }
        let updateResponse = await fetch(URL + "reimbursements", {
            method : 'PUT',
            body: JSON.stringify(updatedReimbursement),
            credentials: 'include'
        });
        if(updateResponse.status === 200)
        {
            console.log("Successfully updated reimbursement");
            window.location.href = "managermenu.html?"+user;
        }
        else
        {
            console.log("Could not update reimbursement");
        }
    }else{
        console.log("Cannot get reimbursement.");
    }
}

async function denyReimbursement()
{
    let reimburseID = sessionStorage.getItem("currentReimbursementID");
    let userID = sessionStorage.getItem("currentLoginID");
    let user = sessionStorage.getItem("currentLoginName");
    let adminUser = await getUser(userID);
    
    let response = await fetch(URL+"reimbursements/"+user+"/"+reimburseID);
    if(response.status === 200){
        let data = await response.json();
        const now = new Date();
        let updatedReimbursement = {
            id : data["id"],
            amount : data["amount"],
            description : data["description"],
            author : data["author"],
            status : "Denied",
            type : data["type"],
            resolver : adminUser,
            submitTime : data["submitTime"],
            resolveTime : now.toUTCString()
        }
        let updateResponse = await fetch(URL + "reimbursements", {
            method : 'PUT',
            body: JSON.stringify(updatedReimbursement),
            credentials: 'include'
        });
        if(updateResponse.status === 200)
        {
            console.log("Successfully updated reimbursement");
            window.location.href = "managermenu.html?"+user;
        }
        else
        {
            console.log("Could not update reimbursement");
        }
    }else{
        console.log("Cannot get reimbursement.");
    }
}

function fillInReimbursementInfo(reimbursement)
{
    let tbody = document.getElementById("reimbursementBody");
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
    switch(reimbursement["status"]){
        case 'Pending':
        tdStatus.innerHTML = "<span style='color:yellow; text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;'>"+"<b>"+reimbursement["status"]+"</b></span>";
        break;
        case 'Approved':
        tdStatus.innerHTML = "<span style='color:green; text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;'>"+"<b>"+reimbursement["status"]+"</b></span>";
        break;
        case 'Denied':
        tdStatus.innerHTML = "<span style='color:red; text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;'>"+"<b>"+reimbursement["status"]+"</b></span>";
        break;
    }
    row.appendChild(tdStatus);

    let tdSubmit = document.createElement("td");
    tdSubmit.setAttribute("class", "col-sm-1");
    tdSubmit.innerText = reimbursement["submitTime"];
    if(!tdSubmit.innerText) tdSubmit.innerText = "N/A";
    row.appendChild(tdSubmit);

    let tdAuthor = document.createElement("td");
    tdAuthor.setAttribute("class", "col-sm-1");
    tdAuthor.innerText = reimbursement["author"].username;
    row.appendChild(tdAuthor);

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