package org.jenkinsci.plugins.extremefeedback.XfManagementLink

import lib.LayoutTagLib
import lib.JenkinsTagLib

def l=namespace(LayoutTagLib)
def t=namespace(JenkinsTagLib)
def st=namespace("jelly:stapler")

l.layout(norefresh: true) {
    l.header() {
        st.bind(var: "it", value: my)

        script(src: "${rootURL}/plugin/extreme-feedback/angular.min.js")
        script(src: "${rootURL}/plugin/extreme-feedback/findlamps.js")
        link(rel: "stylesheet", type: "text/css", href:"${rootURL}/plugin/extreme-feedback/style.css")
    }
    l.main_panel() {
        div(id:"xf-app-container", "ng-app": "xfApp") {
            div("ng-controller": "xfController") {

                h1 {
                    text(my.displayName)
                }

                div {
                    p {
                        text("Order your lamps at ")
                        a(href: "http://www.gitgear.com/xfd") {
                            text("gitgear.com/xfd")
                        }
                    }
                }

                table(class:"sg-choice") {
                    tr(class: "sg-title") {
                        td(colspan:"2") {
                            text("Add Lamps")
                        }
                    }
                    tr(class: "sg-subtitle") {
                        td {
                            text("Automatically")
                        }
                        td {
                            text("Manually")
                        }
                    }
                    tr {
                        td {
                            div(id: "button", "ng-show":"findlampsToggle") {
                                button("ng-click": "findlamps()", "Find lamps in the subnet")
                            }
                            div(id: "spinner", style: "display: none", "ng-hide": "findlampsToggle") {
                                text("Searching...")
                                t.progressBar(pos:-1)
                            }
                        }
                        td {
                            div(id: "add-lamp", "ng-show":"ipToggle") {
                                input(type: "text", id: "add-lamp-input", "ng-model": "ipAddress", "ng-click": "updateIpContent()")
                                button("ng-click": "addLamp(ipAddress);", "Add Lamp")
                            }
                            div(id: "spinner2", style: "display: none", "ng-hide":"ipToggle") {
                                text("Searching...")
                                t.progressBar(pos:-1)
                            }
                        }
                    }
                }

                table(class:"sg-table", style: "text-align: left", "ng-show": "lamps.length") {
                    thead {
                        tr {
                            th {
                                text("Active")
                            }
                            th {
                                text("MAC Address")
                            }
                            th {
                                text("IP Address")
                            }
                            th {
                                text("Name")
                            }
                            th {
                                text("Jobs Assigned To")
                            }
                            th( "ng-mouseover": "showTip(\"Aggregate\")", "ng-mouseleave": "hideTip()") {
                                text("Aggregate")
                                br()
                                text("results")
                                div(class:"sg-tip", "ng-show": "aggregateTipIsVisible"){
                                    p("The Aggregate option combines results all the jobs tied to a lamp " +
                                            "to a single status.")
                                    p {
                                        text("The overall result will always be the ")
                                        em("lowest denominator")
                                        text(" meaning if you have 3 jobs tied to the lamp and one of the jobs is ")
                                        em("FAILED")
                                        text(" the lamp will show ")
                                        em("red")
                                        text(" as status. ")
                                    }
                                    p("The aggregate status show when no jobs are building")
                                    p("If the aggregate option is off the lamp will display the result of the job that ran last.")
                                }
                            }
                            th( "ng-mouseover": "showTip(\"AggregateBuilding\")", "ng-mouseleave": "hideTip()")  {
                                text("Aggregate")
                                br()
                                text("while building")
                                div(class:"sg-tip", "ng-show": "aggregateBuildingTipIsVisible"){
                                    p("The Aggregate while building option combines results all the jobs tied to a lamp " +
                                            "to a single status and shows this status while tied builds are running.")
                                    p {
                                        text("The overall result will always be the ")
                                        em("lowest denominator")
                                        text(" meaning if you have 3 jobs tied to the lamp and one of the jobs is ")
                                        em("FAILED")
                                        text(" the lamp will show ")
                                        em("red")
                                        text(" as status. ")
                                    }
                                    p("The aggregate while building status will show the combined status when "+
                                            "tied jobs are building")
                                }
                            }
                            th ( "ng-mouseover": "showTip(\"Alarm\")", "ng-mouseleave": "hideTip()")  {
                                text("Alarm")
                                br()
                                div(class:"sg-tip", "ng-show": "alarmTipIsVisible"){
                                    p("Trigger the 'beep' alarm when a tied job is failing.")
                                    p("The Alarm option makes a very noisy beep sound when a build fails.")
                                }
                            }
                            th ( "ng-mouseover": "showTip(\"Blame\")", "ng-mouseleave": "hideTip()")  {
                                text("Blame")
                                div(class:"sg-tip", "ng-show": "blameTipIsVisible"){
                                    p("The blame option will write culprit info in the lamp's display when a build fails.")
                                }
                            }
                            th ( "ng-mouseover": "showTip(\"SFX\")", "ng-mouseleave": "hideTip()")  {
                                text("SFX")
                                div(class:"sg-tip", "ng-show": "sfxTipIsVisible"){
                                    p("If the SFX option is 'on' the Lamp will play a file by random when a " +
                                            "tied job finishes depending on the job status:")
                                    p("The SFX sounds can be configured to some extend: wav/mp3 files can be " +
                                            "uploaded (ssh) to lamp into the folders:")
                                    p {
                                        text("For builds with status: SUCCESS:")
                                        br()
                                        text("/home/pi/extremefeedback/XFD-Audio/Green")
                                    }
                                    p {
                                        text("For builds with status: UNSTABLE:")
                                        br()
                                        text("/home/pi/extremefeedback/XFD-Audio/Yellow")
                                    }
                                    p {
                                        text("For builds with status: FAILED:")
                                        br()
                                        text("/home/pi/extremefeedback/XFD-Audio/Red")
                                    }
                                }
                            }
                            th {
                                text("Remove")
                            }
                        }
                    }
                    tbody {
                        tr("ng-repeat": "lamp in lamps | orderBy:['macAddress']") {
                            td {
                                input(type: "checkbox", "ng-model": "lamp.inactive", "ng-change": "changeLamp(lamp)", "inverted": "")
                            }
                            td {
                                text("{{lamp.macAddress}}")
                            }
                            td {
                                text("{{lamp.ipAddress}}")
                            }
                            td {
                                input(type: "text", "ng-model": "lamp.name", "ng-enter": "changeLamp(lamp)")
                                img(src: "${rootURL}/plugin/extreme-feedback/pencil.png", "ng-click": "changeLamp(lamp)", style: "cursor: pointer;")
                            }
                            td {
                                table(class: "jobs") {
                                    tr("ng-repeat": "job in lamp.jobs", "ng-mouseover": "showRemove = true", "ng-mouseleave": "showRemove = false") {
                                        td {
                                            text("{{job}}")
                                        }
                                        td(align: "right", class: "delete") {
                                            img(src: "${rootURL}/plugin/extreme-feedback/remove.png", "ng-click": "removeProjectFromLamp(job, lamp)", style: "display:none;", "ng-show": "showRemove")
                                        }
                                    }
                                    tr {
                                        td(colspan: "2") {
                                            typeahead("items": "projects", "btntxt": "Add", "context": "lamp", "action": "addProjectToLamp(arg1, arg2)")
                                        }
                                    }
                                }
                            }
                            td {
                                input(type: "checkbox", "ng-model": "lamp.aggregate", "ng-change": "changeLamp(lamp)")
                            }
                            td {
                                input(type: "checkbox", "ng-model": "lamp.aggregateBuilding", "ng-change": "changeLamp(lamp)")
                            }
                            td {
                                input(type: "checkbox", "ng-model": "lamp.noisy", "ng-change": "changeLamp(lamp)")
                            }
                            td {
                                input(type: "checkbox", "ng-model": "lamp.blame", "ng-change": "changeLamp(lamp)")
                            }
                            td {
                                input(type: "checkbox", "ng-model": "lamp.sfx", "ng-change": "changeLamp(lamp)")
                            }
                            td {
                                img(src: "${rootURL}/plugin/extreme-feedback/remove.png", "ng-click": "removeLamp(lamp)", style: "cursor: pointer;")
                            }
                        }
                    }
                }
                div("ng-show": "!lamps.length") {
                    text("No lamps registered yet.")
                }
            }
        }
    }
}


