class Navbar extends HTMLElement {
    constructor() {
        super();
    }
    /* Invoked each time the custom element is appended into a document-connected element. */
    connectedCallback() {
        // TODO: Generate this automatically once localstorage is set up
        const initials = "V6";

        this.innerHTML = `
            <div class="relative flex flex-row h-20 bg-gradient-to-t from-[#1e2328] to-[#191e23] justify-between px-10 py-2 z-50">
                <!-- Wrapper for middle UT logo -->
                <div class="flex items-center h-full w-max">
                    <a href="/leaderboard.html">
                        <img src="/images/ppicon.png" />
                    </a>
                </div>
                <!-- Wrapper for right-hand side -->
                <div class="flex flex-row items-center gap-2">
                        <!-- Dropdown navigation icon -->
                        <div class="dropdown dropdown-end">
                            <label tabindex="0" class="btn btn-ghost btn-circle">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" /></svg>
                            </label>
                            <ul tabindex="0" class="menu menu-compact dropdown-content mt-5 p-2 shadow-md bg-[#1e2328] rounded-md w-52">
                                <li><a href="/leaderboard.html">Home</a></li>
                                <li><a href="/play.html">Play</a></li>
                                <li><a href="/demo.html">Demo</a></li>
                                <div class="divider"></div>
                                <li class="-mt-2"><a onclick="APILogoutCall()" href="/signup.html">Log out</a></li>
                            </ul>
                        </div>
                        <!-- Profile Icon -->
                        <div class="avatar placeholder">
                            <div class="btn btn-ghost btn-circle bg-neutral-focus text-neutral-content w-12">
                                <a href="./profile.html">
                                    <span>${initials}</span>
                                </a>
                            </div>
                        </div> 
                </div>
            </div>
        `;
    }
}

customElements.define("navbar", Navbar);