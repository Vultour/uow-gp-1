# uow-gp-1

This is the repository for the Software Group Development Project.

#GITHUB RULES

Please notify the group before you upload a change, make appropriate optional extended descriptions. If you do not understand how to use GitHub, contact either
**Abubaker, Harsh, Martin** or **Niral**.

#Additional Documents
[Architecture decisions [Google Docs]](https://docs.google.com/a/my.westminster.ac.uk/document/d/1GGT6WK9evuDKc2JazUJtfi_85MhwwiLHgL54npDbii4/edit?usp=sharing)

#Team members (if you forget)
**Abubaker** [Team Leader]

**Harsh** [Software Engineer]

**Martin** [Lead Developer]

**Niral** [GUI Designer]

**Romina** [Chief System Architect]

**Tulga** [Software Tester]

# **Guidelines**

For the next semester we will have everyone start at 100% contribution. This is
to promote a fair basis for us all.

In order to maintain this contribution level, you must not do the following:

* Not attending to meetings or tutorials will result in -30% contribution (per offence).
  * This is, of course taken on a case by case basis.
* Attending but contributing nothing results in -10%.
 * Members of the group must write a summary at the end of each week, this will
   be reviewed by four members of the group. (**Abubaker, Harsh, Martin, Niral**).
   It is necessary for all four to be in agreement for this.
   * The summaries for everyone will be put into each member's *WorkSummary* folder.
     This can be found in the main folder; summaries will be put into this format
     **'date_name'** (typically these should contain the date of each Friday).
     It can be done in any format that is readable (.docx, .pages, etc).
* **Martin** has proposed a working system that allows members to be assigned work;
  this work must be completed by the deadline that is given (i.e. a week).
  * Failure to do so will result in penalties and will reflect at the end of the
    module as this is specifically tracking each member's tasks. This will again
    be discussed by the four members mentioned above.
  * We will be using the bugtracking software Mantis for this. You can find our
    instance on http://sdgp.nydus.eu - you'll be able to find all issues (tasks)
    assigned to you there.

It is perfectly fine to ask for help. But to wait on others to do it for you will
not be tolerated. In the group summary (weekly) this will be reflected again.

These guidelines will be updated with new rules as we continue so please notify
**Niral** to add rules to them if the rest of the group sees them as worth using.


# API Endpoints
## List of monitored nodes
http://tunnel-sdgp.nydus.eu:8080/dashboard?context=nodes

## Node system info (OS only currently)
http://tunnel-sdgp.nydus.eu:8080/dashboard?context=sysinfo&node=NODE_ID

## List of network adapters on a node
http://tunnel-sdgp.nydus.eu:8080/dashboard?context=netadapters&node=NODE_ID

## Ingress & egress data of an adapter
http://tunnel-sdgp.nydus.eu:8080/dashboard?context=netusage&adapter=ADAPTER_ID
