const URL = "http://localhost:8081/";

//let addReimbursementButton = document.getElementById('addReimbursementButton');

//addReimbursementButton.onclick = addReimbursement;

async function getReimbursementsByFilterString(filter)
{
  let response = await fetch(URL+"reimbursements" + filter);
  if(response.status === 200){
    let data = await response.json();
    return data;
  }else{
    console.log("Cannot get reimbursements.");
  }
}

async function getReimbursements()
{
  let data = await getReimbursementsByFilterString("");
  populateReimbursementsTable(data, true, false);
}

async function getPendingReimbursements()
{
  let data = await getReimbursementsByFilterString("/statuses/Pending");
  let countText = document.getElementById("pendingCount");
  countText.innerText = Object.keys(data).length + " pending request(s).";
  populateReimbursementsTable(data, true, true);
}

async function getPastRequests()
{
  let data = await getReimbursementsByFilterString("/statuses/past");
  populateReimbursementsTable(data, true, false);
}

async function getApprovedRequests()
{
  let data = await getReimbursementsByFilterString("/statuses/Approved");
  populateReimbursementsTable(data, true, false);
}

async function getDeniedRequests()
{
  let data = await getReimbursementsByFilterString("/statuses/Denied");
  populateReimbursementsTable(data, true, false);
}

async function getReimbursementsFromUser()
{
  let user = sessionStorage.getItem("currentLoginName");
  let data = await getReimbursementsByFilterString("/"+user);
  populateReimbursementsTable(data, false, false);
}

async function getPendingReimbursementsFromUser()
{
  let user = sessionStorage.getItem("currentLoginName");
  let data = await getReimbursementsByFilterString("/statuses/Pending/"+user);
  populateReimbursementsTable(data, false, false);
}

async function getApprovedReimbursementsFromUser()
{
  let user = sessionStorage.getItem("currentLoginName");
  let data = await getReimbursementsByFilterString("/statuses/Approved/"+user);
  populateReimbursementsTable(data, false, false);
}

async function getDeniedReimbursementsFromUser()
{
  let user = sessionStorage.getItem("currentLoginName");
  let data = await getReimbursementsByFilterString("/statuses/Denied/"+user);
  populateReimbursementsTable(data, false, false);
}

function getRequestsByFilter()
{
  let select = document.getElementById("filterBy");
  switch(select.value)
  {
    case "1":
      getPastRequests();
      break;
    case "2":
      getApprovedRequests();
      break;
    case "3":
      getDeniedRequests();
      break;
  }
}

function getUserRequestsByFilter()
{
  let select = document.getElementById("filterBy");
  switch(select.value)
  {
    case "1":
      getReimbursementsFromUser();
      break;
    case "2":
      getPendingReimbursementsFromUser();
      break;
    case "3":
      getApprovedReimbursementsFromUser();
      break;
    case "4":
      getDeniedReimbursementsFromUser();
      break;
  }
}

function populateReimbursementsTable(data, displayAuthor, canClick){
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
  if(canClick)
  {
    $('.clickable-row').click(function () {
      let id = $(this).find('td:first').text();
      sessionStorage.setItem("currentReimbursementID", parseInt(id));
      window.location.href = "reimbursement.html?"+id;
    });
    $('.clickable-row').on(
      {mouseenter : function(){
      $(this).css({"transform": "scale(1)",
      "-webkit-transform":"scale(1)",
      "-moz-transform":"scale(1)",
      "box-shadow":"0px 0px 5px rgba(0,0,0,0.3)",
      "-webkit-box-shadow":"0px 0px 5px rgba(0,0,0,0.3)",
      "-moz-box-shadow":"0px 0px 5px rgba(0,0,0,0.3)"});
    },
      mouseleave: function(){
        $(this).css({"transform": "",
      "-webkit-transform":"",
      "-moz-transform":"",
      "box-shadow":"",
      "-webkit-box-shadow":"",
      "-moz-box-shadow":""});}
    });
  }
}