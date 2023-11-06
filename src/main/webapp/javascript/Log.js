class Log extends HTMLElement {
    constructor() {
        super();
    }
    /* Invoked each time the custom element is appended into a document-connected element. */
    connectedCallback() {
        /* Leaderboard data must be a json object */
        const logData = this.attributes.logData?.value;
        const leaderboardDataDecoded = JSON.parse(logData);

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

        const headerAdminName = document.createElement("th");
        headerAdminName.classList.add("w-12", "px-6", "py-3");
        headerAdminName.innerHTML = "admin";

        const headerCellAction = document.createElement("th");
        headerCellAction.classList.add("w-64", "px-6", "py-3");
        headerCellAction.innerHTML = "action";

        const headerCellUser = document.createElement("th");
        headerCellUser.classList.add("px-6", "py-3");
        headerCellUser.innerHTML = "user";

        const headerCellTime = document.createElement("th");
        headerCellTime.classList.add("px-6", "py-3");
        headerCellTime.innerHTML = "date_of_record";


        tableHeadRow.append(headerAdminName, headerCellAction, headerCellUser,headerCellTime);
        tableHead.appendChild(tableHeadRow);

        /* Table body */
        const tableBody = document.createElement("tbody");

        /* Going over all log data entries */
        leaderboardDataDecoded.forEach((data) => {
            const tr = document.createElement("tr");
            tr.classList.add("bg-white", "border-b");

            /* Creating table data cells */
            const bodyCellAdmin = document.createElement("td");
            bodyCellAdmin.classList.add("flex", "justify-between", "items-center", "px-6", "py-4");
            bodyCellAdmin.innerHTML = data.admin_id;

            const bodyCellAction = document.createElement("td");
            bodyCellAction.classList.add("px-6", "py-4");
            bodyCellAction.innerHTML = data.action_type;

            const bodyCellToUser = document.createElement("td");
            bodyCellToUser.classList.add("px-6", "py-4");
            bodyCellToUser.innerHTML = data.player_id;

            const bodyCellTime = document.createElement("td");
            bodyCellTime.classList.add("px-6", "py-3");
            bodyCellTime.innerHTML = data.time_of_action;

            tr.append(bodyCellAdmin, bodyCellAction, bodyCellToUser, bodyCellTime);
            tableBody.appendChild(tr);
        });

        /* Finalizing */
        table.append(tableHead, tableBody);
        wrapperDiv.appendChild(table);

        this.appendChild(wrapperDiv);
    }
}

customElements.define("log-table", Log);