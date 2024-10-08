class UsersTable extends HTMLElement {
    constructor() {
        super();
    }
    /* Invoked each time the custom element is appended into a document-connected element. */
    connectedCallback() {
        /* Leaderboard data must be a json object */
        const usersData = this.attributes.usersData?.value;
        const userDataDecoded = JSON.parse(usersData);

        /* Wrapper div for the whole table */
        const wrapperDiv = document.createElement("div");
        wrapperDiv.classList.add("rounded-md", "overflow-hidden");

        /* Table element */
        const table = document.createElement("table");
        table.classList.add("w-full", "text-sm", "text-left", "text-gray-500");

        /* Table head */
        const tableHead = document.createElement("thead");
        tableHead.classList.add("text-xs", "text-gray-700", "uppercase", "bg-gray-100");

        /* Table head row */
        const tableHeadRow = document.createElement("tr");

        const headerCellAction = document.createElement("th");
        headerCellAction.classList.add("w-12", "px-6", "py-3");
        headerCellAction.innerHTML = "action";

        const headerCellBan = document.createElement("th");
        headerCellBan.classList.add("w-12", "px-6", "py-3");
        headerCellBan.innerHTML = "ban";

        const headerCellUsername = document.createElement("th");
        headerCellUsername.classList.add("w-64", "px-6", "py-3");
        headerCellUsername.innerHTML = "username";

        const headerCellEmail = document.createElement("th");
        headerCellEmail.classList.add("px-6", "py-3");
        headerCellEmail.innerHTML = "email";

        const headerCellRole = document.createElement("th");
        headerCellRole.classList.add("px-6", "py-3");
        headerCellRole.innerHTML = "role";


        tableHeadRow.append(headerCellAction, headerCellUsername, headerCellEmail, headerCellRole);
        tableHead.appendChild(tableHeadRow);

        /* Table body */
        const tableBody = document.createElement("tbody");

        /* Going over all leaderboard data entries */
        userDataDecoded.forEach((data) => {

            var name = data.username;

            /* Creating table body row */
            const tr = document.createElement("tr");
            tr.classList.add("bg-white", "border-b");

            /* Creating table data cells */
            const bodyCellAction = document.createElement("td");
            bodyCellAction.classList.add("flex", "justify-between", "items-center", "px-6", "py-4");
            bodyCellAction.innerHTML = "<button id='" + data.username + "' class='btn btn-info w-full sign-button border-none' onclick=callSwitchRoleAndActiveButton(this.id)>Change Role</button>";

            const bodyCellBan = document.createElement("td");
            bodyCellBan.classList.add("flex", "justify-between", "items-center", "px-6", "py-4");
            if(data.active) {
                bodyCellBan.innerHTML = "<button id='" + data.username + "' class='btn btn-info w-full sign-button border-none' onclick=callSwitchBanAndActiveButton(this.id)>Ban</button>";
            } else {
                bodyCellBan.innerHTML = "<button id='" + data.username + "' class='btn btn-info w-full sign-button border-none' onclick=callSwitchBanAndActiveButton(this.id)>Unban</button>";
            }

            const bodyCellUsername = document.createElement("td");
            bodyCellUsername.classList.add("px-6", "py-4");
            bodyCellUsername.innerHTML = data.username;

            const bodyCellEmail = document.createElement("td");
            bodyCellEmail.classList.add("px-6", "py-4");
            bodyCellEmail.innerHTML = data.email;

            const bodyCellRole = document.createElement("td");
            bodyCellRole.classList.add("px-6", "py-3");
            bodyCellRole.innerHTML = data.u_type;

            tr.append(bodyCellAction, bodyCellBan, bodyCellUsername, bodyCellEmail,bodyCellRole);
            tableBody.appendChild(tr);
        });

        /* Finalizing */
        table.append(tableHead, tableBody);
        wrapperDiv.appendChild(table);

        this.appendChild(wrapperDiv);
    }
}

customElements.define("users-table", UsersTable);