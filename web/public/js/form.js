/**
 * Created by lookitsmarc on 15/05/2017.
 */
const formFactory = (() => {

    const fields = Object.assign({}, {
        pwd: `
            <div class="input-group pwd" >
              <span class="input-group-addon" id="password">Password</span>
              <input type="text" class="form-control" placeholder="" name="password" aria-describedby="password">
            </div>
        `,
        mail: `
            <div class="input-group mail">
                <span class="input-group-addon" id="mail">Mail</span>
                <input type="text" class="form-control" placeholder="" name="mail" aria-describedby="mail">
            </div>`,
        date: `
            <div class="form-group row date">
              <label for="example-datetime-local-input" class="col-2 col-form-label">Date and time</label>
              <div class="col-10">
                <input class="form-control" type="datetime-local" value="2011-08-19T13:45:00" id="example-datetime-local-input">
              </div>
            </div>
            <div class="form-group row date">
              <label for="example-datetime-local-input" class="col-2 col-form-label">Date and time</label>
              <div class="col-10">
                <input class="form-control" type="datetime-local" value="2011-08-19T13:45:00" id="example-datetime-local-input">
              </div>
            </div>
        `    
    });

    /**
     * Add Component
     * @param {String} type
     */
    const addComponent = (type, DOMBind) => {
        console.log(fields[type]);
        document.getElementById(DOMBind).insertAdjacentHTML('beforeend', fields[type]);
    };

    /**
     * Rm Component
     * @param {String} type
     */
    const rmComponent = (target) => {
        let rmtarg = document.getElementsByClassName(target);
        
        rmtarg.map(DOMElement => {
            DOMElement.remove();
        });
    };

    /**
     * Init Component
     */
    const initComponent = () => {
        document.addEventListener('DOMContentLoaded', () => {
            let DOMLists = document.getElementsByClassName('addon');

            for (let list of DOMLists) {
                list.addEventListener('click', addComponent.bind(null, list.getAttribute('data-id'), 'urlform'));
            }
        });
    };

    // Init the universe
    initComponent();

    return {
        addcomponent: addComponent,
        rmcomponent: rmComponent,
    }
})();